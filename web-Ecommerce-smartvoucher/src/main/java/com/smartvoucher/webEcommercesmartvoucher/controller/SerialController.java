package com.smartvoucher.webEcommercesmartvoucher.controller;

import com.smartvoucher.webEcommercesmartvoucher.dto.SerialDTO;
import com.smartvoucher.webEcommercesmartvoucher.payload.ResponseObject;
import com.smartvoucher.webEcommercesmartvoucher.service.ISerialService;
import com.smartvoucher.webEcommercesmartvoucher.service.impl.SerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serial")
public class SerialController {

    private ISerialService serialService;

    @Autowired
    public SerialController(SerialService serialService) {
        this.serialService = serialService;
    }

    @GetMapping("")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllSerial() throws Exception{

        ResponseObject responseObject = serialService.getAllSerial();

        return ResponseEntity.status(responseObject.getStatusCode()).body(responseObject);
    }

    @PostMapping()
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public ResponseEntity<?> insertSerial(@RequestBody SerialDTO serialDTO) throws Exception {

        ResponseObject responseObject = serialService.insertSerial(serialDTO);

        return ResponseEntity.status(responseObject.getStatusCode()).body(responseObject);
    }

    @PutMapping()
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public ResponseEntity<?> updateSerial(@RequestBody SerialDTO serialDTO) throws Exception {

        ResponseObject responseObject = serialService.updateSerial(serialDTO);

        return ResponseEntity.status(responseObject.getStatusCode()).body(responseObject);
    }

    @DeleteMapping()
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public ResponseEntity<?> deleteSerial(@RequestParam long id) throws Exception {

        ResponseObject responseObject = serialService.deleteSerial(id);

        return ResponseEntity.status(responseObject.getStatusCode()).body(responseObject);
    }
}