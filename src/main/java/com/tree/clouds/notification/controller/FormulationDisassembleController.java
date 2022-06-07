package com.tree.clouds.notification.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.common.aop.Log;
import com.tree.clouds.notification.model.bo.FormulationDisassembleBO;
import com.tree.clouds.notification.model.entity.FormulationDisassemble;
import com.tree.clouds.notification.model.vo.FormulationDisassembleVO;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;
import com.tree.clouds.notification.model.vo.PublicIdReqVO;
import com.tree.clouds.notification.model.vo.PublicIdsReqVO;
import com.tree.clouds.notification.service.FormulationDisassembleService;
import com.tree.clouds.notification.utils.LoginUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 指标拆解表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/disassemble")
@Api(value = "formulation-disassemble", tags = "指标拆解模块")
public class FormulationDisassembleController {
    @Autowired
    private FormulationDisassembleService disassembleService;

    @PostMapping("/disassemblePage")
    @ApiOperation(value = "各地区工作指标分页查询")
    @Log("各地区工作指标分页查询")
    @PreAuthorize("hasAuthority('formulation:disassemble:list')")
    public RestResponse<IPage<FormulationDisassembleBO>> disassemblePage(@RequestBody FormulationDrawPageVO formulationDrawPageVO) {
        IPage<FormulationDisassembleBO> page = disassembleService.disassemblePage(formulationDrawPageVO, LoginUserUtil.getUserRegion());
        return RestResponse.ok(page);
    }

    @GetMapping("/exportData/{year}/{regionId}")
    @ApiOperation(value = "导出工作指标")
    @Log("导出工作指标")
    public void exportData(HttpServletResponse response, @PathVariable Integer year, @PathVariable Integer regionId) {
        disassembleService.exportData(year, regionId, response);
    }

    @PostMapping("/detailDisassemble")
    @ApiOperation(value = "分配指标拆解信息查询 drawId")
    @Log("分配指标拆解信息查询")
    @PreAuthorize("hasAuthority('formulation:disassemble:list')")
    public RestResponse<List<FormulationDisassemble>> detailDisassemble(@RequestBody PublicIdReqVO publicIdReqVO) {
        List<FormulationDisassemble> list = disassembleService.detailDisassemble(publicIdReqVO.getId());
        return RestResponse.ok(list);
    }

    @PostMapping("/saveDisassemble")
    @ApiOperation(value = "保存分配指标")
    @Log("保存分配指标")
    @PreAuthorize("hasAuthority('formulation:disassemble:save')")
    public RestResponse<Boolean> saveDisassemble(@RequestBody List<FormulationDisassembleVO> formulationDisassembles) {
        disassembleService.saveDisassemble(formulationDisassembles);
        return RestResponse.ok(true);
    }

    @PostMapping("/rebuildDisassemble/{disassembleId}")
    @ApiOperation(value = "重置分配指标")
    @Log("重置分配指标")
//    @PreAuthorize("hasAuthority('formulation:disassemble:save')")
    public RestResponse<Boolean> rebuildDisassemble(@PathVariable String disassembleId) {
        disassembleService.rebuildDisassemble(disassembleId);
        return RestResponse.ok(true);
    }

    @PostMapping("/releaseDisassemble")
    @ApiOperation(value = "发布指标")
    @Log("发布指标")
    @PreAuthorize("hasAuthority('formulation:disassemble:release')")
    public RestResponse<Boolean> releaseDisassemble(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        disassembleService.releaseDisassemble(publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }
}

