package com.cmci.common.util;

import com.cmci.common.service.CommonApiService;
import com.cmci.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("com.cmci.common.util.CommonUtil")
public class CommonUtil {

    @Autowired
    private CommonApiService apiService;

    public byte[] getPythonResultSsim() {
        String url = "http://localhost:5000/result_ssim";
        return apiService.callExternalImageApi(url);
    }
}
