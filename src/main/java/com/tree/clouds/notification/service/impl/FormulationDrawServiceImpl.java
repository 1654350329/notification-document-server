package com.tree.clouds.notification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.common.Constants;
import com.tree.clouds.notification.mapper.FormulationDrawMapper;
import com.tree.clouds.notification.model.entity.FormulationDisassemble;
import com.tree.clouds.notification.model.entity.FormulationDraw;
import com.tree.clouds.notification.model.vo.DataInfoVO;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;
import com.tree.clouds.notification.model.vo.FormulationDrawVO;
import com.tree.clouds.notification.model.vo.MonthInfoVO;
import com.tree.clouds.notification.service.FormulationDisassembleService;
import com.tree.clouds.notification.service.FormulationDrawService;
import com.tree.clouds.notification.service.MonthInfoService;
import com.tree.clouds.notification.utils.BaseBusinessException;
import com.tree.clouds.notification.utils.DownloadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * <p>
 * 指标制定表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Service
public class FormulationDrawServiceImpl extends ServiceImpl<FormulationDrawMapper, FormulationDraw> implements FormulationDrawService {

    @Autowired
    private FormulationDisassembleService disassembleService;
    @Autowired
    private MonthInfoService monthInfoService;
    @Autowired
    private FormulationDisassembleService formulationDisassembleService;

    @Override
    public IPage<FormulationDraw> drawPage(FormulationDrawPageVO formulationDrawPageVO) {
        IPage<FormulationDraw> page = formulationDrawPageVO.getPage();
        page = this.baseMapper.drawPage(page, formulationDrawPageVO);
        page.getRecords().forEach(formulationDraw -> {
            //获取发布地区数量
            int distributionCount = this.baseMapper.distributionCount(formulationDraw.getDrawId());
            List<MonthInfoVO> monthInfoVOS = monthInfoService.getByBizId(formulationDraw.getDrawId());
            formulationDraw.setMonthInfoVOS(monthInfoVOS);
            formulationDraw.setDistribution(distributionCount);
            //一共11个地区
            formulationDraw.setUnDistribution(11 - distributionCount);
        });
        return page;
    }

    public int distributionCount(String drawId) {
        return this.baseMapper.distributionCount(drawId);
    }

    @Override
    public void updateSort(String drawId, Integer startSort) {
        FormulationDraw draw = this.getById(drawId);
        if (Objects.equals(draw.getSort(), startSort)) {
            return;
        }
        List<FormulationDraw> draws = new ArrayList<>();
        if (draw.getSort() > startSort) {
            draws = this.baseMapper.getBySortAndYear(startSort, draw.getSort(), draw.getYear());
        }
        if (draw.getSort() < startSort) {
            draws = this.baseMapper.getBySortAndYear(draw.getSort(), startSort, draw.getYear());
        }
        for (FormulationDraw formulationDraw : draws) {
            if (formulationDraw.getDrawId().equals(drawId)) {
                formulationDraw.setSort(startSort);
                continue;
            }
            if (draw.getSort() > startSort) {
                formulationDraw.setSort(formulationDraw.getSort() + 1);
            }
            if (draw.getSort() < startSort) {
                formulationDraw.setSort(formulationDraw.getSort() - 1);
            }
        }
        this.updateBatchById(draws);
    }

    /**
     * 判断本年度是否已发布项目
     */
    public void check() {
        List<FormulationDraw> list = this.list(new QueryWrapper<FormulationDraw>().eq(
                FormulationDraw.YEAR, DateUtil.year(new Date()))
                .eq(FormulationDraw.STATUS, 1));
        if (CollUtil.isNotEmpty(list)) {
            if (list.get(0).getStatus() == 1) {
                throw new BaseBusinessException(400, "本年度已完成发布,不许添加新指标项目!");
            }
        }
    }

    @Override
    public void addDraw(FormulationDrawVO formulationDrawVO) {
        //判断本年度是否已发布项目
//        check();
        FormulationDraw one = this.getOne(new QueryWrapper<FormulationDraw>().eq(FormulationDraw.YEAR, DateUtil.year(new Date()))
                .orderByDesc(FormulationDraw.SORT)
                .last("limit 1"));
        FormulationDraw formulationDraw = BeanUtil.toBean(formulationDrawVO, FormulationDraw.class);
        formulationDraw.setYear(DateUtil.year(new Date()));
        formulationDraw.setDistributeStatus(0);
        formulationDraw.setStatus(0);
        formulationDraw.setSort(one == null ? 1 : one.getSort() + 1);
        this.save(formulationDraw);
        monthInfoService.addByBizId(formulationDrawVO.getMonthInfoVOS(), formulationDraw.getDrawId());
        disassembleService.initDisassemble(formulationDraw.getDrawId());
    }

    @Override
    public void updateDraw(FormulationDrawVO formulationDrawVO) {
        FormulationDraw draw = this.getById(formulationDrawVO.getDrawId());
        if (draw.getStatus() != null && draw.getStatus() == 1) {
            throw new BaseBusinessException(400, "已发布不可修改!!");
        }
        FormulationDraw formulationDraw = BeanUtil.toBean(formulationDrawVO, FormulationDraw.class);
        this.updateById(formulationDraw);
        monthInfoService.updateByBizId(formulationDrawVO.getMonthInfoVOS(), formulationDrawVO.getDrawId());
    }

    @Override
    public void exportDraw(Integer year, HttpServletResponse response) {
        FormulationDrawPageVO formulationDrawPageVO = new FormulationDrawPageVO();
        formulationDrawPageVO.setYear(year);
        IPage<FormulationDraw> formulationDrawIPage = drawPage(formulationDrawPageVO);
        List<FormulationDraw> records = formulationDrawIPage.getRecords();
        String resource = this.getClass().getClassLoader().getResource("drowFile.xlsx").getFile();
        String fileName = year + "年全市人社系統重点工作指标进度及成效评价一览表.xlsx";
        Map<String, Object> head = new HashMap<>();
        head.put("year", year);
        List<Map<String, Object>> dates = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            FormulationDraw draw = records.get(i);
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
    @Transactional
    public void copyDraw(Integer year) {
        //判断本年度是否已发布项目
        check();
        //传值为零复制上一年数据
        if (year == 0) {
            year = DateUtil.year(new Date()) - 1;
        }
        QueryWrapper<FormulationDraw> formulationDrawQueryWrapper = new QueryWrapper<>();
        formulationDrawQueryWrapper.eq(FormulationDraw.YEAR, year);
        List<FormulationDraw> list = this.list(formulationDrawQueryWrapper);
        //复制基础数据
        list.forEach(formulationDraw -> {
            String oldDrawID = formulationDraw.getDrawId();
            if (formulationDraw.getYear() == DateUtil.year(new Date())) {
                throw new BaseBusinessException(400, "不能复制本年数据");
            }
            formulationDraw.setYear(DateUtil.year(new Date()));
            formulationDraw.setDrawId(null);
            formulationDraw.setCreateTime(null);
            formulationDraw.setUpdateTime(null);
            formulationDraw.setCreateUser(null);
            formulationDraw.setUpdateUser(null);
            this.save(formulationDraw);
            this.monthInfoService.copyData(oldDrawID, formulationDraw.getDrawId());
        });

    }

    @Override
    public List<DataInfoVO> dataInfo(Integer year, String drawId) {
        return this.baseMapper.dataInfo(year, drawId);
    }

    @Override
    public void removeDraw(String id) {
        FormulationDraw draw = this.getById(id);
        if (draw.getStatus() != null && draw.getStatus() == 1) {
            throw new BaseBusinessException(400, "已发布不可删除!");
        }
        this.removeById(id);
        monthInfoService.deleteByBizId(id);
        disassembleService.remove(new QueryWrapper<FormulationDisassemble>().eq(FormulationDisassemble.DRAW_ID, id));
    }

    @Override
    public FormulationDraw getDrawInfo(String id) {
        FormulationDraw draw = this.getById(id);
        //获取发布地区数量
        int distributionCount = this.baseMapper.distributionCount(id);
        List<MonthInfoVO> monthInfoVOS = monthInfoService.getByBizId(id);
        draw.setMonthInfoVOS(monthInfoVOS);
        draw.setDistribution(distributionCount);
        //一共10个地区
        draw.setUnDistribution(10 - distributionCount);
        return draw;
    }

    @Override
    public FormulationDraw getByDisassembleId(String disassembleId) {
        FormulationDisassemble formulationDisassemble = formulationDisassembleService.getById(disassembleId);
        return this.getById(formulationDisassemble.getDrawId());
    }
}
