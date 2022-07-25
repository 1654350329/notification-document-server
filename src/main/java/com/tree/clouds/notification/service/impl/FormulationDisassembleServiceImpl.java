package com.tree.clouds.notification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.common.Constants;
import com.tree.clouds.notification.mapper.FormulationDisassembleMapper;
import com.tree.clouds.notification.model.bo.FormulationDisassembleBO;
import com.tree.clouds.notification.model.entity.DataReport;
import com.tree.clouds.notification.model.entity.FormulationDisassemble;
import com.tree.clouds.notification.model.entity.FormulationDraw;
import com.tree.clouds.notification.model.entity.Region;
import com.tree.clouds.notification.model.vo.FormulationDisassembleVO;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;
import com.tree.clouds.notification.model.vo.MonthInfoVO;
import com.tree.clouds.notification.service.*;
import com.tree.clouds.notification.utils.BaseBusinessException;
import com.tree.clouds.notification.utils.DownloadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 指标拆解表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Service
public class FormulationDisassembleServiceImpl extends ServiceImpl<FormulationDisassembleMapper, FormulationDisassemble> implements FormulationDisassembleService {
    @Autowired
    private RegionService regionService;
    @Autowired
    private DataReportService dataReportService;
    @Autowired
    private FormulationDrawService drawService;
    @Autowired
    private MonthInfoService monthInfoService;


    @Override
    public IPage<FormulationDisassembleBO> disassemblePage(FormulationDrawPageVO formulationDrawPageVO, Integer regionId) {
        IPage<FormulationDisassembleBO> page = formulationDrawPageVO.getPage();
        page = this.baseMapper.disassemblePage(page, formulationDrawPageVO, regionId);
        page.getRecords().forEach(record -> {
            record.setMonthInfoVOS(this.monthInfoService.getByBizId(record.getDisassembleId()));
        });
        return page;
    }

    @Override
    public List<FormulationDisassemble> detailDisassemble(String drawId) {
        List<FormulationDisassemble> list = this.baseMapper.detailDisassemble(drawId);
        list.forEach(formulationDisassemble -> formulationDisassemble.setMonthInfoVOS(monthInfoService.getByBizId(formulationDisassemble.getDisassembleId())));
        return list;
    }

    @Override
    @Transactional
    public void saveDisassemble(List<FormulationDisassembleVO> formulationDisassembles) {

        ArrayList<FormulationDisassemble> disassembles = new ArrayList<>();
        List<String> disassembleIds = formulationDisassembles.stream().map(FormulationDisassembleVO::getDisassembleId).collect(Collectors.toList());

        formulationDisassembles.forEach(disassemble -> {
            FormulationDisassemble bean = BeanUtil.toBean(disassemble, FormulationDisassemble.class);
            FormulationDraw draw = drawService.getById(disassemble.getDrawId());
            Region region = regionService.getById(disassemble.getRegionId());
            if (draw.getNumberType() == 0 || draw.getNumberType() == 1) {
                if (!NumberUtil.isNumber(disassemble.getTask())) {
                    throw new BaseBusinessException(400, "任务类型为数值或百分比,只允许填写数字!");
                }
//                for (MonthInfoVO monthInfoVO : disassemble.getMonthInfoVOS()) {
//                    if (!NumberUtil.isNumber(monthInfoVO.getData())) {
//                        throw new BaseBusinessException(400, "任务类型为数值或百分比,只允许填写数字!");
//                    }
//                }
            }
            //数值型判断是否单个大于全年任务
            if (draw.getNumberType() == 0) {
                double taskCount = formulationDisassembles.stream().mapToDouble(formulationDisassemble -> Double.parseDouble(formulationDisassemble.getTask())).sum();
                if (Double.parseDouble(disassemble.getTask()) > Double.parseDouble(draw.getTask())) {
                    throw new BaseBusinessException(400, "考核单位:" + region.getRegionName() + "数据超出全市任务目标，请重新编辑！");
                }
                QueryWrapper<FormulationDisassemble> wrapper = new QueryWrapper<>();
                wrapper.eq(FormulationDisassemble.DRAW_ID, draw.getDrawId());
                List<FormulationDisassemble> list = this.list(wrapper);
                Double count = list.stream().filter(formulationDisassemble -> !disassembleIds.contains(formulationDisassemble.getDisassembleId())).mapToDouble(formulationDisassemble -> {
                    if (formulationDisassemble.getTask() == null) {
                        return 0;
                    }
                    return Double.parseDouble(formulationDisassemble.getTask());
                }).sum();

                if (Double.parseDouble(draw.getTask()) < taskCount + count && formulationDisassembles.size() == 1) {
                    throw new BaseBusinessException(400, "考核单位:" + region.getRegionName() + "保存本次数据将超出全市任务目标，请重新编辑！");
                }
                if (Double.parseDouble(draw.getTask()) < taskCount + count && formulationDisassembles.size() > 1) {
                    throw new BaseBusinessException(400, "保存本次数据将超出全市任务目标，请重新编辑！");
                }
                double dataCount = disassemble.getMonthInfoVOS().stream().filter(monthInfoVO -> monthInfoVO.getMonth() == 12).mapToDouble(monthInfoVO ->
                        Double.parseDouble(monthInfoVO.getData())
                ).sum();
                if (Double.parseDouble(disassemble.getTask()) != dataCount) {
                    throw new BaseBusinessException(400, "考核单位:" + region.getRegionName() + "各月份数据不等于全年任务，请重新编辑！");
                }
            }
            bean.setStatus(1);
            disassembles.add(bean);
            monthInfoService.updateByBizId(disassemble.getMonthInfoVOS(), disassemble.getDisassembleId());
            //更新分配状态
            draw.setDistributeStatus(2);
            this.drawService.updateById(draw);
        });
        this.updateBatchById(disassembles);
        String drawId = formulationDisassembles.get(0).getDrawId();
//        //判断是否已完全分配完成
        getCompleteStatus(drawId);
    }

    private void getCompleteStatus(String drawId) {
        List<FormulationDisassemble> list = this.list(new QueryWrapper<FormulationDisassemble>().eq(FormulationDisassemble.DRAW_ID, drawId));
        long count = list.stream().filter(formulationDisassemble -> formulationDisassemble.getStatus() == 0).count();
        FormulationDraw draw = new FormulationDraw();
        draw.setDrawId(drawId);
        if (count == 0) {
            draw.setDistributeStatus(1);
        }
        if (count != 0 && count < list.size()) {
            draw.setDistributeStatus(2);
        }
        if (count == list.size()) {
            draw.setDistributeStatus(0);
        }
        this.drawService.updateById(draw);
    }

    @Override
    @Transactional
    public void releaseDisassemble(List<String> ids) {
        List<FormulationDraw> list = drawService.listByIds(ids);
        for (FormulationDraw draw : list) {
            if (draw.getStatus() == 1) {
                continue;
            }
            if (11 != drawService.distributionCount(draw.getDrawId())) {
                throw new BaseBusinessException(400, draw.getDrawName() + "还有未分配单位,不许发布");
            }
            draw.setStatus(1);//修改为已发布
            drawService.updateById(draw);
            //查找已暂存
            QueryWrapper<FormulationDisassemble> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(FormulationDisassemble.DRAW_ID, draw.getDrawId());
            queryWrapper.eq(FormulationDisassemble.STATUS, 1);
            List<FormulationDisassemble> disassembles = this.list(queryWrapper);
            disassembles.forEach(disassemble -> {
                //任务上报没有 才需要新增
                if (CollUtil.isNotEmpty(dataReportService.list(new QueryWrapper<DataReport>().eq(DataReport.DISASSEMBLE_ID, disassemble.getDisassembleId())))) {
                    return;
                }
                disassemble.setReleaseStatus(1);
                DataReport dataReport = new DataReport();
                dataReport.setDrawName(draw.getDrawName());
                dataReport.setTask(disassemble.getTask());
                dataReport.setYear(draw.getYear());
                dataReport.setRegionId(disassemble.getRegionId());
                dataReport.setDisassembleId(disassemble.getDisassembleId());
                dataReportService.save(dataReport);
                this.monthInfoService.addByBizId(null, dataReport.getReportId());
            });
            this.updateBatchById(disassembles);
        }

    }

    @Override
    public void initDisassemble(String drawId) {
        List<Region> regionList = regionService.list();
        for (Region region : regionList) {
            FormulationDisassemble disassemble = new FormulationDisassemble();
            disassemble.setDrawId(drawId);
            disassemble.setRegionId(region.getRegionId());
            disassemble.setStatus(0);
            this.save(disassemble);
            monthInfoService.addByBizId(null, disassemble.getDisassembleId());
        }

    }

    @Override
    public void exportData(Integer year, Integer regionId, HttpServletResponse response) {
        Region region = regionService.getById(regionId);
        FormulationDrawPageVO formulationDrawPageVO = new FormulationDrawPageVO();
        formulationDrawPageVO.setYear(year);
        List<FormulationDisassembleBO> records = disassemblePage(formulationDrawPageVO, regionId).getRecords();
        InputStream in = this.getClass().getResourceAsStream("/drowFile.xlsx");
//        String resource = this.getClass().getClassLoader().getResource("drowFile.xlsx").getFile();
        String fileName = year + "年" + region.getRegionName() + "工作指标综述.xlsx";
        Map<String, Object> head = new HashMap<>();
        head.put("year", year);
        List<Map<String, Object>> dates = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            FormulationDisassembleBO draw = records.get(i);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("index", i + 1);
            data.put("drawName", draw.getDrawName());
            data.put("leader", draw.getDrawName());
            data.put("task", draw.getTask());
            for (int i1 = 0; i1 < draw.getMonthInfoVOS().size(); i1++) {
                MonthInfoVO monthInfoVO = draw.getMonthInfoVOS().get(i1);
                data.put("m" + (i1 + 1), monthInfoVO.getData());
            }
            dates.add(data);

        }
        ExcelWriter excelWriter = EasyExcel.write(Constants.TMP_HOME + fileName)
                .withTemplate(in)
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(dates, writeSheet);
        excelWriter.fill(head, writeSheet);
        excelWriter.finish();
        byte[] bytes = DownloadFile.File2byte(new File(Constants.TMP_HOME + fileName));
        DownloadFile.downloadFile(bytes, fileName, response, false);
    }

    @Override
    public void rebuildDisassemble(String disassembleId) {
        //清空各月份数据
        List<MonthInfoVO> monthInfoVOS = this.monthInfoService.getByBizId(disassembleId);
        monthInfoVOS.forEach(monthInfoVO -> monthInfoVO.setData("/"));
        this.monthInfoService.updateByMonthId(monthInfoVOS);
        //修改地区为未分配
        FormulationDisassemble disassemble = this.getById(disassembleId);
        disassemble.setTask(null);
        disassemble.setStatus(0);
        this.updateById(disassemble);
        getCompleteStatus(disassemble.getDrawId());
    }

    @Override
    public List<FormulationDisassemble> getByDrawId(String drawId) {

        return this.list(new QueryWrapper<FormulationDisassemble>().eq(FormulationDisassemble.DRAW_ID, drawId));
    }

}
