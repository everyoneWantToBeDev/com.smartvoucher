package com.smartvoucher.webEcommercesmartvoucher.service;

import com.google.api.services.drive.model.File;
import com.smartvoucher.webEcommercesmartvoucher.dto.MerchantDTO;
import com.smartvoucher.webEcommercesmartvoucher.dto.WareHouseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IWareHouseService {
    List<WareHouseDTO> getAllWareHouse();
    List<WareHouseDTO> getAllWareHouseCode(WareHouseDTO wareHouseDTO);
    WareHouseDTO upsert(WareHouseDTO wareHouseDTO);
    void deleteWareHouse(WareHouseDTO wareHouseDTO);
    Boolean existWareHouse(WareHouseDTO wareHouseDTO);
    Boolean existCategoryAndDiscount(WareHouseDTO wareHouseDTO);
    Boolean existMerchantAndLabel(WareHouseDTO wareHouseDTO);
    File uploadWarehouseImages(MultipartFile fileName);
}
