package com.oyyo.gmall.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.CategoryEntity;
import com.oyyo.gmall.pms.service.CategoryService;
import com.oyyo.gmall.pms.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 商品三级分类
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:37
 */
@Api(tags = "商品三级分类 管理")
@RestController
@RequestMapping("pms/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父分类id 查询子分类集合
     * @param pid
     * @return
     */
    @GetMapping("{pid}")
    public Resp<List<CategoryVO>> querySubCategories(@PathVariable("pid") Long pid){

        List<CategoryVO> categoryVOS = categoryService.querySubCategories(pid);

        return Resp.ok(categoryVOS);
    }

    /**
     * 分类查询
     * @param level
     * @param pid
     * @return
     */
    @GetMapping
    public Resp<List<CategoryEntity>> queryCategoriesByPidOrLevel(
            @RequestParam(value = "level",defaultValue = "0")Integer level,
            @RequestParam(value = "parentCid",required = false)Long pid){
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        //判断分类的级别
        if (level != 0) {
            queryWrapper.eq("cat_level",level);
        }
        //判断父节点的id是否为空
        if (pid != null) {
            queryWrapper.eq("parent_cid",pid);
        }

        List<CategoryEntity> categoryEntityList = categoryService.list(queryWrapper);
        return Resp.ok(categoryEntityList);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:category:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = categoryService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{catId}")
    @PreAuthorize("hasAuthority('pms:category:info')")
    public Resp<CategoryEntity> info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return Resp.ok(category);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:category:save')")
    public Resp<Object> save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:category:update')")
    public Resp<Object> update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:category:delete')")
    public Resp<Object> delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));

        return Resp.ok(null);
    }

}
