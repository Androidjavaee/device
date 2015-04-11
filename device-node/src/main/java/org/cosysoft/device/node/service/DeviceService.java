package org.cosysoft.device.node.service;

import org.cosysoft.device.DeviceStore;
import org.cosysoft.device.android.AndroidDevice;
import org.cosysoft.device.android.impl.AndroidDeviceStore;
import org.cosysoft.device.image.ImageUtils;
import org.cosysoft.device.model.DeviceInfo;
import org.cosysoft.device.node.domain.Result;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 兰天 on 2015/4/8.
 */
@Service
public class DeviceService {

    private DeviceStore deviceStore = AndroidDeviceStore.getInstance();

    @Cacheable(value = "avatars")
    public byte[] getAvatar(String serialId) {
        AndroidDevice device = deviceStore.getDeviceBySerial(serialId);
        BufferedImage image = device.takeScreenshot();
        return ImageUtils.toByteArray(image);
    }

    public byte[] takeScreenShot(String serialId) {
        AndroidDevice device = deviceStore.getDeviceBySerial(serialId);
        BufferedImage image = device.takeScreenshot();
        return ImageUtils.toByteArray(image);
    }

    @Cacheable(value = "devices")
    public List<DeviceInfo> getDevices() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        deviceStore.getDevices().forEach(device -> {
            deviceInfos.add(device.getDeviceInfo());
        });
        return deviceInfos;
    }

    public Result<String> runAdbCommand(String serialId, String cmd) {
        Result<String> result = new Result<>();
        AndroidDevice device = deviceStore.getDeviceBySerial(serialId);
        String out = device.runAdbCommand(cmd);
        result.setPayload(out);
        return result;
    }
}
