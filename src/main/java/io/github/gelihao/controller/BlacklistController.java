package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.entity.Blacklist;
import io.github.gelihao.service.BlacklistService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/blacklist")
public class BlacklistController {

    @Resource
    private BlacklistService blacklistService;

    @GetMapping("/fetchlist")
    public CommonResult<Object> getList(@RequestParam Map listQuery){
        System.out.println("listQuery = " + listQuery);
        Page<Blacklist> blacklistPage = blacklistService.pageByListQuery(listQuery);
        Map<String,Object> data = new HashMap<>();
        data.put("total", blacklistPage.getTotal());
        data.put("items", blacklistPage.getRecords());

        return CommonResult.success(data);

    }

    @PostMapping("/delete")
    public CommonResult<Object> delete(@RequestBody Blacklist blacklist){
        QueryWrapper<Blacklist> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(blacklist.getCompanyname()), "companyname", blacklist.getCompanyname());
        queryWrapper.like(StringUtils.isNotBlank(blacklist.getIdentifier()), "identifier", blacklist.getIdentifier());
        boolean remove = blacklistService.remove(queryWrapper);
        if (remove) {
            return CommonResult.success("删除黑名单成功");
        }
        else {
            return CommonResult.failed("删除失败");
        }

    }

    @PostMapping("/addblacklist")
    public CommonResult<String> addBlacklist(@RequestBody Blacklist blacklist) {
        System.out.println("请求添加的黑名单 = " + blacklist);
        blacklist.setJoindate(new Date());
        QueryWrapper<Blacklist> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(blacklist.getCompanyname()), "companyname", blacklist.getCompanyname());
        queryWrapper.like(StringUtils.isNotBlank(blacklist.getIdentifier()), "identifier", blacklist.getIdentifier());
        boolean save = blacklistService.saveOrUpdate(blacklist,queryWrapper);
        if (save) {
            return CommonResult.success("添加黑名单成功");
        }
        else {
            return CommonResult.failed("添加黑名单失败");
        }
    }
}
