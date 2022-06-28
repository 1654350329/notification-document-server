package com.tree.clouds.notification.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.common.Constants;
import com.tree.clouds.notification.mapper.FormulationLogMapper;
import com.tree.clouds.notification.model.entity.FormulationDraw;
import com.tree.clouds.notification.model.entity.FormulationLog;
import com.tree.clouds.notification.model.vo.DataInfoVO;
import com.tree.clouds.notification.model.vo.FormulationLogPageVO;
import com.tree.clouds.notification.model.vo.FormulationLogVO;
import com.tree.clouds.notification.model.vo.RegionDataVO;
import com.tree.clouds.notification.service.FormulationDrawService;
import com.tree.clouds.notification.service.FormulationLogService;
import com.tree.clouds.notification.utils.DownloadFile;
import com.tree.clouds.notification.utils.excel.ExcelMergeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-11
 */
@Service
public class FormulationLogServiceImpl extends ServiceImpl<FormulationLogMapper, FormulationLog> implements FormulationLogService {
    @Autowired
    private FormulationDrawService formulationDrawService;

    @Override
    public IPage<FormulationLog> formulationLogPage(FormulationLogPageVO formulationLogPageVO) {
        IPage<FormulationLog> page = formulationLogPageVO.getPage();
        return this.baseMapper.formulationLogPage(page, formulationLogPageVO);
    }

    @Override
    public FormulationLogVO formulationLogDetail(String logId) {
        FormulationLogVO formulationLogVO = new FormulationLogVO();
        FormulationLog formulationLog = this.getById(logId);
        formulationLogVO.setFormulationLog(formulationLog);
        List<DataInfoVO> ts = JSONUtil.toList(new JSONArray(formulationLog.getDataInfo()), DataInfoVO.class);
        formulationLogVO.setDataInfoVOS(ts);
        return formulationLogVO;
    }

    @Override
    public void exportFormulationLog(String id, HttpServletResponse response) {
        FormulationLogVO formulationLogVO = formulationLogDetail(id);
        InputStream resource = this.getClass().getResourceAsStream("/dataFile.xlsx");
        FormulationLog formulationLog = formulationLogVO.getFormulationLog();
        String date = formulationLog.getMonth() == 1 ? formulationLog.getYear() + "年 1月" : formulationLog.getYear() + "年 1月-" + formulationLog.getMonth() + "月";

        String fileName = date + "全市人社系統重点工作指标进度及成效评价一览表.xlsx";
        Map<String, Object> head = new HashMap<>();
        head.put("data", date);
        List<DataInfoVO> dataInfoVOS = formulationLogVO.getDataInfoVOS();
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
                data.put("status" + i1, dataVO.getStatus());
            }
            dates.add(data);

        }
        ExcelWriter excelWriter = EasyExcel.write(Constants.TMP_HOME + fileName)
                .withTemplate(resource)
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(dates, writeSheet);
        excelWriter.fill(head, writeSheet);
        excelWriter.finish();

        byte[] bytes = DownloadFile.File2byte(new File(Constants.TMP_HOME + fileName));
        DownloadFile.downloadFile(bytes, fileName, response, false);

    }

    @Override
    public void exportLog(String logId, String drawId, HttpServletResponse response) {
        FormulationLogVO formulationLogVO = formulationLogDetail(logId);
        InputStream in = this.getClass().getResourceAsStream("/drawDetailFile.xlsx");
        FormulationLog formulationLog = formulationLogVO.getFormulationLog();
//        String fileName = formulationLog.getTitle() + ".xlsx";
        FormulationDraw draw = this.formulationDrawService.getById(drawId);
        String date;
        if (formulationLog.getMonth() > 1) {
            date = draw.getDrawName() + draw.getYear() + "年 1月-" + formulationLog.getMonth() + "月";
        } else {
            date = draw.getDrawName() + draw.getYear() + "年 1月";
        }
        String fileName = date + "全市人社系統重点工作指标进度及成效评价一览表.xlsx";
        List<Map<String, Object>> dates = new ArrayList<>();
        List<DataInfoVO> dataInfoVOS = formulationLogVO.getDataInfoVOS().stream().filter(dataInfoVO -> dataInfoVO.getDrawId().equals(drawId)).collect(Collectors.toList());
        if (CollUtil.isEmpty(dataInfoVOS)) {
            return;
        }
        DataInfoVO dataInfoVO = dataInfoVOS.get(0);
        for (int i1 = 0; i1 < dataInfoVO.getRegionDataVOS().size(); i1++) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("index", i1 + 1);
            data.put("drawName", dataInfoVO.getDrawName());
            RegionDataVO dataVO = dataInfoVO.getRegionDataVOS().get(i1);
            data.put("regionName", dataVO.getRegionName());
            data.put("task", dataVO.getTask());
            data.put("data", dataVO.getData());
            data.put("dataSum", dataVO.getDataSum());
            data.put("progress", dataVO.getProgress());
            if (dataVO.getProgress() != null) {
                String replace = dataVO.getProgress().replace("/", "").replace("%", "");
                if (StrUtil.isBlank(replace)) {
                    data.put("status", 0);
                }
                if (NumberUtil.isNumber(replace)) {
                    data.put("status", Double.parseDouble(replace) > 100 ? "完成" : dataVO.getStatus() == 0 ? "滞后" : "序时");
                } else {
                    data.put("status", 0);
                }
            }
            data.put("ranking", dataVO.getRanking());

            dates.add(data);

        }
        List<Integer> list = new ArrayList<>();
        list.add(1);
        ExcelWriter excelWriter = EasyExcel.write(Constants.TMP_HOME + fileName)
                .withTemplate(in)
                .registerWriteHandler(new ExcelMergeStrategy(1, list, 3))
                .build();

        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(dates, writeSheet);
        excelWriter.finish();

        byte[] bytes = DownloadFile.File2byte(new File(Constants.TMP_HOME + fileName));
        DownloadFile.downloadFile(bytes, fileName, response, false);
    }
}
