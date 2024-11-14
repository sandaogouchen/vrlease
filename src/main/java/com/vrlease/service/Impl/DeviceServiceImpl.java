package com.vrlease.service.Impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.vrlease.dto.DeviceAddDTO;
import com.vrlease.dto.DevicePageDTO;
import com.vrlease.dto.DeviceUpdateDTO;
import com.vrlease.dto.QueryBrandDTO;
import com.vrlease.entity.Device;
import com.vrlease.mapper.DeviceMapper;
import com.vrlease.service.DeviceService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vrlease.util.RedisConstants.DEVICE_BRAND_KEY;
import static com.vrlease.util.RedisConstants.DEVICE_KEY;
import static java.lang.Math.min;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public String addDevice(DeviceAddDTO deviceAddDTO) {
        Device device = new Device();
        device.setName(deviceAddDTO.getName());
        device.setBrandId(deviceAddDTO.getBrandId());
        device.setImages(deviceAddDTO.getImages());
        device.setSold(deviceAddDTO.getSold());
        device.setCreateTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());
        deviceMapper.insert(device);
        Map map = new HashMap();
        map.put("success","添加成功");
        return JSONUtil.toJsonStr(map);
    }

    @Override
    public String updateDevice(DeviceUpdateDTO deviceUpdateDTO) {
        Long id = deviceUpdateDTO.getId();
        Device test = deviceMapper.selectById(id);
        if (test==null){
            Map map = new HashMap();
            map.put("error","设备不存在");
            return JSONUtil.toJsonStr(map);
        }
        Device device = new Device();
        device.setName(deviceUpdateDTO.getName());
        device.setBrandId(deviceUpdateDTO.getBrandId());
        device.setImages(deviceUpdateDTO.getImages());
        device.setSold(deviceUpdateDTO.getSold());
        device.setUpdateTime(LocalDateTime.now());
        stringRedisTemplate.delete(DEVICE_KEY+id);
        deviceMapper.updateById(device,id);
        Map map = new HashMap();
        map.put("success","修改成功");
        return JSONUtil.toJsonStr(map);
    }

    @Override
    public String pageQuery(DevicePageDTO devicePageDTO) {
        List<Device> devices = deviceMapper.pageQuery(devicePageDTO.getPage(), devicePageDTO.getSize());
        Map map = new HashMap();
        Integer page = devicePageDTO.getPage();
        Integer size = devicePageDTO.getSize();
        if((page-1)*size > devices.size() && page*size > devices.size()){
            map.put("-1","没有更多数据了");
            return JSONUtil.toJsonStr(map);
        }
        Integer pos = min(page*size,devices.size());
        for(Integer i = (page-1)*size;i<pos;i++){
            map.put(i+1,devices.get(i));
        }
        return JSONUtil.toJsonStr(map);
    }

    @Override
    public String query(Long id) {
        Map map = new HashMap();
        String s = stringRedisTemplate.opsForValue().get(DEVICE_KEY + id);
        if(s == null) {
            Device device = deviceMapper.selectById(id);
            stringRedisTemplate.opsForValue().set(DEVICE_KEY+id,JSONUtil.toJsonStr(device));
            map.put("device",device);
        }
        else {
            map.put("device", s);
        }
        return JSONUtil.toJsonStr(map);
    }

    @Override
    public String queryBybrand(QueryBrandDTO queryBrandDTO) {
        Long brandId = queryBrandDTO.getBrandId();
        String s = stringRedisTemplate.opsForValue().get(DEVICE_BRAND_KEY + brandId);
        Map map = new HashMap();
        if(s == null) {
            List<Device> devices = deviceMapper.queryBybrand(brandId);
            stringRedisTemplate.opsForValue().set(DEVICE_BRAND_KEY+brandId,JSONUtil.toJsonStr(devices));
            map.put("devices",devices);
        }
        else {
            map.put("devices", s);
        }
        return JSONUtil.toJsonStr(map);
    }

    public Device query1(Long id){//穿透
        String str = stringRedisTemplate.opsForValue().get(DEVICE_KEY + id);

        if(StrUtil.isNotBlank(str)){//存过redis的正常情况
            Device device = JSONUtil.toBean(str, Device.class);
            return device;
        }
        if(str != null){
            return null;
        }
        Device device = deviceMapper.selectById(id);//新增入缓存

        if(device == null){
            //写入空值
            stringRedisTemplate.opsForValue().set(DEVICE_KEY +id , "", 3L, java.util.concurrent.TimeUnit.MINUTES);
            return null;
        }

        stringRedisTemplate.opsForValue().set(DEVICE_KEY+id , JSONUtil.toJsonStr(device));

        return device;
    }
}
