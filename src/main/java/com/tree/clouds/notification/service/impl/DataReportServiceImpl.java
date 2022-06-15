package com.tree.clouds.notification.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.common.Constants;
import com.tree.clouds.notification.mapper.DataReportMapper;
import com.tree.clouds.notification.model.entity.*;
import com.tree.clouds.notification.model.vo.*;
import com.tree.clouds.notification.service.*;
import com.tree.clouds.notification.utils.BaseBusinessException;
import com.tree.clouds.notification.utils.DownloadFile;
import com.tree.clouds.notification.utils.LoginUserUtil;
import com.tree.clouds.notification.utils.ValueComparator;
import com.tree.clouds.notification.utils.excel.ExcelMergeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据上报 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Service
public class DataReportServiceImpl extends ServiceImpl<DataReportMapper, DataReport> implements DataReportService {

    @Autowired
    private FormulationDrawService formulationDrawService;
    @Autowired
    private MonthInfoService monthInfoService;
    @Autowired
    private FormulationDisassembleService formulationDisassembleService;
    @Autowired
    private ReportRecordService reportRecordService;
    @Autowired
    private FormulationLogService formulationLogService;

    public static void main(String[] args) {
        double i = Double.parseDouble(-90 + "") / 120;
        System.out.println("i = " + i);
    }

    @Override
    public IPage<DataReport> dataReportPage(FormulationDrawPageVO drawPageVO, Integer regionId) {
        IPage<DataReport> page = drawPageVO.getPage();
        regionId = regionId == null ? LoginUserUtil.getUserRegion() : regionId;
        IPage<DataReport> dataReportIPage = this.baseMapper.dataReportPage(page, drawPageVO, regionId);
        dataReportIPage.getRecords().forEach(record -> {

            FormulationDraw draw = this.formulationDrawService.getByDisassembleId(record.getDisassembleId());
            record.setNumberType(draw.getNumberType());
            List<MonthInfoVO> monthInfoVOS = monthInfoService.getByBizId(record.getReportId());
            int month = 1;
            if (drawPageVO.getYear() != DateUtil.year(new Date())) {
                month = 12;
            } else {
                month = Math.max(DateUtil.month(new Date()), 1);
            }
            int finalMonth = month;
            List<String> collect = monthInfoVOS.stream().filter(monthInfoVO -> monthInfoVO.getMonth() == finalMonth).map(MonthInfoVO::getData).collect(Collectors.toList());
            //大于当前月份不让上报
            monthInfoVOS.stream().filter(monthInfoVO -> monthInfoVO.getMonth() > finalMonth).forEach(monthInfoVO -> monthInfoVO.setExamineStatus(999));
            if (CollUtil.isNotEmpty(collect)) {
                String value = collect.get(0);
                record.setProgress(value);
            }
            String sum = getSum(monthInfoVOS, draw.getNumberType());
            if (draw.getNumberType() != 0 && sum.equals("0.0")) {
                sum = "/";
            }
            record.setSum(sum);
            if (draw.getNumberType() == 2 || draw.getNumberType() == 1) {
                record.setSumProgress("/");
            } else {
                record.setSumProgress(getSumProgress(record.getSum(), record.getTask()));
            }
            if (draw.getNumberType() == 1) {
                monthInfoVOS.stream().filter(monthInfoVO -> monthInfoVO.getData().equals("/")).forEach(monthInfoVO -> monthInfoVO.setData(null));
            }

            record.setMonthInfoVOS(monthInfoVOS);
        });
        return dataReportIPage;
    }

    private String getSumProgress(String sum, String task) {
        if (NumberUtil.isNumber(sum) && NumberUtil.isNumber(task)) {
            double s = Double.parseDouble(sum);
            double t = Double.parseDouble(task);
            return NumberUtil.formatPercent(s / t, 2);
        }
        return null;
    }

    private String getSum(List<MonthInfoVO> monthInfoVOS, int type) {

        double sum = 0.00;
        int i = 0;
        for (MonthInfoVO monthInfoVO : monthInfoVOS) {
            if (monthInfoVO.getData() != null && NumberUtil.isNumber(monthInfoVO.getData())) {
                sum += Double.parseDouble(monthInfoVO.getData());
                i++;
            }
        }
        if (type == 1 && i != 0) {
            return NumberUtil.formatPercent((sum / 100) / i, 2);
        }
        return String.valueOf(sum);
    }

    @Override
    public void addDataReport(DataReportVO dataReportVO) {
        DataReport dataReport = this.getById(dataReportVO.getReportId());
        FormulationDisassemble disassemble = this.formulationDisassembleService.getById(dataReport.getDisassembleId());
        FormulationDraw draw = this.formulationDrawService.getById(disassemble.getDrawId());
        for (MonthInfoVO monthInfoVO : dataReportVO.getMonthInfoVOS()) {
            if (draw.getNumberType() == 0 || draw.getNumberType() == 1) {
                if (!NumberUtil.isNumber(monthInfoVO.getData())) {
                    throw new BaseBusinessException(400, "只允许填写数值型");
                }
            }
            if (monthInfoVO.getExamineStatus() > 2) {
                throw new BaseBusinessException(400, "已提交审核,不许修改!");
            }
//            if (monthInfoVO.getMonth() > DateUtil.month(new Date()) + 1 && monthInfoVO.getData() != null) {
//                throw new BaseBusinessException(400, "不许提交未到期数据!");
//            }
            monthInfoVO.setExamineStatus(1);
        }
        dataReport.setRegionId(LoginUserUtil.getUserRegion());
        this.updateById(dataReport);
        this.monthInfoService.updateByMonthId(dataReportVO.getMonthInfoVOS());
        List<String> monthIds = dataReportVO.getMonthInfoVOS().stream().map(MonthInfoVO::getMonthId).collect(Collectors.toList());
        reportRecordService.addRecord(monthIds, "上报");
    }

    @Override
    public IPage<ReportExamineVO> reportExaminePage(IPage<ReportExamineVO> page, ReportExaminePageVO reportExaminePageVO) {

        if (reportExaminePageVO.getRegionId() == null) {
            reportExaminePageVO.setRegionId(LoginUserUtil.getUserRegion());
        }
        IPage<ReportExamineVO> reportExamineVOIPage = this.baseMapper.reportExaminePage(page, reportExaminePageVO, reportExaminePageVO.getRegionId());
        reportExamineVOIPage.getRecords().forEach(record -> {
            FormulationDraw draw = formulationDrawService.getByDisassembleId(record.getDisassembleId());
            String data = record.getData();
            if (draw.getNumberType() == 1) {
                record.setTask(draw.getCalculationType() + record.getTask() + "%");
                data = data + "%";
            }
            record.setNumberType(draw.getNumberType());
            record.setOrderProgress(Constants.initMap(record.getMonth()));
            if (draw.getNumberType() == 0) {
                record.setProgress(getSumProgress(record.getData(), record.getTask()));
            } else {
                record.setProgress("/");
            }
            record.setMonthProgress(data);
        });
        return reportExamineVOIPage;
    }

    /**
     * 修改通报状态
     *
     * @param year
     * @param month
     */
    @Override
    @Transactional
    public void addReleaseExamine(Integer year, Integer month) {
        List<String> monthIds = baseMapper.listByDate(year, month).stream().map(MonthInfo::getMonthId).collect(Collectors.toList());
        if (CollUtil.isEmpty(monthIds)) {
            throw new BaseBusinessException(400, "当前年没有可通报数据");
        }
        this.monthInfoService.updateExamineStatus(monthIds);
        //添加到通报日志
        String date = month == 1 ? year + "年1月" : year + "年1月 - " + month + "月";
        FormulationLog formulationLog = new FormulationLog();
        formulationLog.setFormulationTime(DateUtil.formatDate(new Date()));
        formulationLog.setTitle(date + "全市人社系统重点工作指标进度及成效评价一览表");
        formulationLog.setYear(year);
        formulationLog.setMonth(month);
        List<DataInfoVO> dataInfoVOS = this.dataInfo(formulationLog.getYear(), formulationLog.getMonth());
        formulationLog.setDataInfo(JSONUtil.toJsonStr(dataInfoVOS));
        formulationLogService.save(formulationLog);
    }

    public DataInfoVO getNumberType(DataInfoVO dataInfoVO, int month) {
        //全市数据
        long allData = 0L;
        long allDataSum = 0L;
        //存放前三地区
        Map<Integer, Double> map = new HashMap<>();
        for (RegionDataVO regionDataVO : dataInfoVO.getRegionDataVOS()) {
            DataReport dataReport = this.getByDisassembleIdAndRegionId(regionDataVO.getDisassembleId(), regionDataVO.getRegionId());
            String data = null;
            if (dataReport != null && dataReport.getReportId() != null) {
                data = monthInfoService.getDataByMonth(dataReport.getReportId(), month);
                if (NumberUtil.isNumber(data)) {
                    allData += Double.parseDouble(data);
                }
            }

            //按地区获取累计完成
            QueryWrapper<DataReport> wrapper = new QueryWrapper<>();
            wrapper.eq(DataReport.DISASSEMBLE_ID, regionDataVO.getDisassembleId());
            wrapper.eq(DataReport.REGION_ID, regionDataVO.getRegionId());
            List<String> reportIds = this.list(wrapper).stream().map(DataReport::getReportId).collect(Collectors.toList());
            String dataSum = null;
            if (CollUtil.isNotEmpty(reportIds)) {
                dataSum = monthInfoService.getDataSum(reportIds, month);
            }
            if (StrUtil.isNotBlank(regionDataVO.getTask()) && dataSum != null) {
                allDataSum += Long.parseLong(dataSum);
            }
            regionDataVO.setData(data);//当月进度
            regionDataVO.setDataSum(dataSum);//累计完成
            regionDataVO.setMonth(month);
            if (StrUtil.isNotBlank(regionDataVO.getTask()) && StrUtil.isNotBlank(dataSum) && NumberUtil.isNumber(regionDataVO.getTask()) && NumberUtil.isNumber(dataSum)) {
                String format = "0.00";
                double number = Double.parseDouble(dataSum) / Double.parseDouble(regionDataVO.getTask());
                if (!regionDataVO.getTask().equals("0")) {
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    format = decimalFormat.format(number * 100);
                    getData(month, regionDataVO, Double.parseDouble(format));
                }
                map.put(regionDataVO.getRegionId(), Double.parseDouble(format));
            } else {
                map.put(regionDataVO.getRegionId(), 0.00);
            }
        }
        //设置旗子状态
        updateStatus(dataInfoVO, map, 0, 1);
        //统计全市数据
        RegionDataVO dataVO = new RegionDataVO();
        FormulationDraw draw = formulationDrawService.getById(dataInfoVO.getDrawId());
        dataVO.setTask(draw.getTask());
        dataVO.setRegionId(0);
        dataVO.setRegionName("全市");
        dataVO.setMonth(month);
        dataVO.setData(String.valueOf(allData));
        dataVO.setDataSum(String.valueOf(allDataSum));
        try {
            Pattern pattern = Pattern.compile("[0-9]*");
            if (draw.getTask() != null && pattern.matcher(draw.getTask()).matches()) {
                double number = allDataSum / Double.parseDouble(draw.getTask());
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String format = decimalFormat.format(number * 100);
                getData(month, dataVO, Double.parseDouble(format));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        dataInfoVO.getRegionDataVOS().add(0, dataVO);
        return dataInfoVO;
    }

    @Override
    public List<DataInfoVO> dataInfo(int year, int month) {
        List<DataInfoVO> dataInfoVOS = formulationDrawService.dataInfo(year);
        //各月份数据
        for (DataInfoVO dataInfoVO : dataInfoVOS) {
            //如果为数字类型
            if (dataInfoVO.getNumberType() == 0) {
                getNumberType(dataInfoVO, month);
            }
            //为百分比类型
            if (dataInfoVO.getNumberType() == 1) {
                getPercentageType(dataInfoVO, month);
            }
            //为文本类型
            if (dataInfoVO.getNumberType() == 2) {
                getTextType(dataInfoVO, month);
            }
        }
        return dataInfoVOS;
    }

    private void getTextType(DataInfoVO dataInfoVO, int month) {
        FormulationDraw draw = this.formulationDrawService.getById(dataInfoVO.getDrawId());
        double allDataSum = 0;
        for (RegionDataVO regionDataVO : dataInfoVO.getRegionDataVOS()) {
            DataReport dataReport = this.getByDisassembleIdAndRegionId(regionDataVO.getDisassembleId(), regionDataVO.getRegionId());
            if (dataReport != null && dataReport.getReportId() != null) {
                String data = monthInfoService.getDataByMonth(dataReport.getReportId(), month);
                regionDataVO.setData(data);
                double dataSum = monthInfoService.getDataByMonth(dataReport.getReportId(), month, 1);
                allDataSum += dataSum;
                regionDataVO.setDataSum(dataSum == 0.0 ? "/" : String.valueOf(dataSum));
                regionDataVO.setProgress("/");
            }
        }
        //统计全市数据
        RegionDataVO dataVO = new RegionDataVO();
        dataVO.setRegionId(0);
        dataVO.setRegionName("全市");
        dataVO.setMonth(month);
        dataVO.setTask(draw.getTask());
        dataVO.setData("/");
        dataVO.setProgress("/");
        dataVO.setDataSum(allDataSum == 0.0 ? "/" : String.valueOf(allDataSum));
        dataVO.setStatus(null);

        dataInfoVO.getRegionDataVOS().add(0, dataVO);
    }

    private void getPercentageType(DataInfoVO dataInfoVO, int month) {
        //存放前三地区
        Map<Integer, Double> map = new HashMap<>();
        FormulationDraw draw = this.formulationDrawService.getById(dataInfoVO.getDrawId());
        double dataSum = 0L;
        for (RegionDataVO regionDataVO : dataInfoVO.getRegionDataVOS()) {
            DataReport dataReport = this.getByDisassembleIdAndRegionId(regionDataVO.getDisassembleId(), regionDataVO.getRegionId());
            if (dataReport != null && dataReport.getReportId() != null) {
                double data = monthInfoService.getDataByMonth(dataReport.getReportId(), month, 1);
                regionDataVO.setDataSum(data + "%");
                map.put(regionDataVO.getRegionId(), data);
                dataSum += data;
                if (draw.getCalculationType().equals(">=") || draw.getCalculationType().equals(">")) {
                    if (NumberUtil.compare(Double.parseDouble(dataReport.getTask()), data) == 1) {
                        regionDataVO.setStatus(0);
                    } else {
                        regionDataVO.setStatus(1);
                    }
                }
                regionDataVO.setTask(draw.getCalculationType() + dataReport.getTask() + "%");
            }
            regionDataVO.setProgress("/");
            regionDataVO.setData("/");
        }
        if (draw.getCalculationType().equals(">=") || draw.getCalculationType().equals(">")) {
            updateStatus(dataInfoVO, map, 0, 2);
        } else {
            updateStatus(dataInfoVO, map, 1, 2);
        }

        //统计全市数据
        RegionDataVO dataVO = new RegionDataVO();
        dataVO.setTask(draw.getCalculationType() + draw.getTask() + "%");
        dataVO.setRegionId(0);
        dataVO.setRegionName("全市");
        dataVO.setMonth(month);
        dataVO.setData("/");
        dataVO.setDataSum(NumberUtil.formatPercent((dataSum / 100) / 11, 2));
        dataVO.setProgress("/");
        if (draw.getCalculationType().equals(">=") || draw.getCalculationType().equals(">")) {
            if (NumberUtil.compare(Double.parseDouble(draw.getTask()), (dataSum / 100) / 11) == 1) {
                dataVO.setStatus(0);
            } else {
                dataVO.setStatus(1);
            }
        }
        if (draw.getCalculationType().equals("<=") || draw.getCalculationType().equals("<")) {
            if (NumberUtil.compare(Double.parseDouble(draw.getTask()), (dataSum / 100) / 11) == 1) {
                dataVO.setStatus(1);
            } else {
                dataVO.setStatus(0);
            }
        }
        dataInfoVO.getRegionDataVOS().add(0, dataVO);
    }

    private void updateStatus(DataInfoVO dataInfoVO, Map<Integer, Double> map, int type, int type2) {
        //移除市本级 市本级不加入排名
        map.remove(1);
        Map<Integer, Double> sortByValue = ValueComparator.sortMapByValues(map, type);
        Collection<Double> values = sortByValue.values();
        //获取所有数据排名
        List<Object> asList = Arrays.asList(new LinkedHashSet<>(values).toArray());
        //前三名值
        List<Object> arrayList = new ArrayList<>();
        for (int i = 0; i < asList.size(); i++) {
            arrayList.add(asList.get(i));
            if (i == 2) {
                break;
            }
        }

        List<Integer> integerList = new ArrayList<>();
        Object[] objects = sortByValue.keySet().toArray();
        List<Object> integers = Arrays.asList(objects);
        for (Object integer : integers) {
            if (arrayList.contains(sortByValue.get(integer))) {
                integerList.add((Integer) integer);
            }
        }
        Map<String, Integer> countList = ValueComparator.countList(values);
        List<String> numberKey = Arrays.asList(countList.keySet().toArray(new String[0]));
        //前三为红旗 否则为蓝旗
        for (RegionDataVO regionDataVO : dataInfoVO.getRegionDataVOS()) {
            if (regionDataVO.getRegionId() == 1) {
                continue;
            }
            int rank = 0;
            for (String number : numberKey) {
                if (regionDataVO.getDataSum() != null && NumberUtil.isNumber(regionDataVO.getDataSum()) && Double.parseDouble(regionDataVO.getDataSum()) < 0) {
                    regionDataVO.setRanking(10);
                    continue;
                }

                String replace;
                if (type2 == 1) {
                    if (regionDataVO.getProgress() == null) {
                        regionDataVO.setRanking(10);
                        continue;
                    }
                    replace = regionDataVO.getProgress().replace("%", "");
                } else {
                    if (regionDataVO.getDataSum() == null) {
                        regionDataVO.setRanking(10);
                        continue;
                    }
                    replace = regionDataVO.getDataSum().replace("%", "");
                }
                if (replace.equals("0.0") || replace.equals("0.00")) {
                    regionDataVO.setRanking(10);
                    continue;
                }
                if (NumberUtil.isNumber(replace) && Double.parseDouble(number) == Double.parseDouble(replace)) {
                    regionDataVO.setRanking(rank + 1);
                    break;
                }


                rank += countList.get(number);
            }
        }
    }

    private void getData(int month, RegionDataVO regionDataVO, double number) {
        regionDataVO.setProgress(number + "%");//进度
        String initMap = Constants.initMap(month);
        if ((number) < Float.parseFloat(initMap.replace("%", ""))) {
            regionDataVO.setStatus(0);//成效评价 1红2蓝
        } else {
            regionDataVO.setStatus(1);
        }
        if (number > 100) {
            regionDataVO.setStatus(2);
        }
    }

    @Override
    public void exportFormulationLog(int year, int month, HttpServletResponse response) {
        InputStream in = this.getClass().getResourceAsStream("/dataFile.xlsx");
        String date;
        if (month > 1) {
            date = year + "年 1月-" + month + "月";
        } else {
            date = year + "年 1月";
        }
        String fileName = date + "全市人社系統重点工作指标进度及成效评价一览表.xlsx";
        Map<String, Object> head = new HashMap<>();
        head.put("data", date);
        List<DataInfoVO> dataInfoVOS = dataInfo(year, month);
        List<Map<String, Object>> dates = new ArrayList<>();

        for (int i = 0; i < dataInfoVOS.size(); i++) {
            DataInfoVO dataInfoVO = dataInfoVOS.get(i);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("index", i + 1);
            data.put("projectName", dataInfoVO.getProjectName());
            data.put("drawName", dataInfoVO.getDrawName());
            data.put("leader", dataInfoVO.getLeader());
            for (int i1 = 0; i1 < dataInfoVO.getRegionDataVOS().size(); i1++) {
                RegionDataVO dataVO = dataInfoVO.getRegionDataVOS().get(i1);
                data.put("task" + i1, dataVO.getTask());
                data.put("data" + i1, dataVO.getData());
                data.put("dataSum" + i1, dataVO.getDataSum());
                data.put("progress" + i1, dataVO.getProgress());
                if (dataVO.getProgress() != null) {
                    String replace = dataVO.getProgress().replace("/", "").replace("%", "");
                    if (StrUtil.isBlank(replace)) {
                        data.put("status", 0);
                    }
                    if (NumberUtil.isNumber(replace)) {
                        data.put("status" + i1, Double.parseDouble(replace) > 100 ? "完成" : dataVO.getStatus() == 0 ? "滞后" : "序时");
                    } else {
                        data.put("status", 0);
                    }
                }
                data.put("ranking" + i1, dataVO.getRanking());
            }
            dates.add(data);

        }
        List<Integer> list = new ArrayList<>();
        list.add(1);
//        int[] k={1};
        ExcelWriter excelWriter = EasyExcel.write(Constants.TMP_HOME + fileName)
                .withTemplate(in)
                .registerWriteHandler(new ExcelMergeStrategy(2, list, 3))
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(dates, writeSheet);
        excelWriter.fill(head, writeSheet);
        excelWriter.finish();

        byte[] bytes = DownloadFile.File2byte(new File(Constants.TMP_HOME + fileName));
        DownloadFile.downloadFile(bytes, fileName, response, false);

    }

    @Override
    public void exportDataReport(Integer year, Integer regionId, HttpServletResponse response) {
        FormulationDrawPageVO formulationDrawPageVO = new FormulationDrawPageVO();
        formulationDrawPageVO.setYear(year);
        List<DataReport> records = dataReportPage(formulationDrawPageVO, regionId).getRecords();
        String fileName = "上报数据" + DateUtil.formatDate(new Date()) + ".xlsx";
        EasyExcel.write(Constants.TMP_HOME + fileName, DataReport.class).sheet("上报数据")
                .doWrite(records);
        byte[] bytes = DownloadFile.File2byte(new File(Constants.TMP_HOME + fileName));
        DownloadFile.downloadFile(bytes, fileName, response, false);
    }

    @Override
    public IPage<ReportExamineVO> reviewExaminePage(IPage<ReportExamineVO> page, ReportExaminePageVO reportExaminePageVO) {
        IPage<ReportExamineVO> reportExamineVOIPage = this.baseMapper.reviewExaminePage(page, reportExaminePageVO);
        reportExamineVOIPage.getRecords().forEach(record -> {
            FormulationDraw draw = formulationDrawService.getByDisassembleId(record.getDisassembleId());
            String data = record.getData();
            if (draw.getNumberType() == 1) {
                record.setTask(draw.getCalculationType() + record.getTask() + "%");
                data = data + "%";
            }
            record.setData(data);
            record.setNumberType(draw.getNumberType());
            record.setOrderProgress(Constants.initMap(record.getMonth()));
            if (draw.getNumberType() == 0) {
                record.setProgress(getSumProgress(record.getData(), record.getTask()));
            } else {
                record.setProgress("/");
            }
            record.setMonthProgress(record.getData());
        });
        return reportExamineVOIPage;
    }

    @Override
    public DataReport getByDisassembleIdAndRegionId(String disassembleId, Integer regionId) {
        return this.getOne(new QueryWrapper<DataReport>().eq(FormulationDisassemble.DISASSEMBLE_ID, disassembleId)
                .eq(FormulationDisassemble.REGION_ID, regionId));

    }
}
