package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.repository.VehicleRepository;
import com.suryaenergi.sdm.backendapi.request.ApproveOrderGARequest;
import com.suryaenergi.sdm.backendapi.request.ApproveOrderRequest;
import com.suryaenergi.sdm.backendapi.request.OrderVehicleRequest;
import com.suryaenergi.sdm.backendapi.request.RejectOrderRequest;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import com.suryaenergi.sdm.backendapi.response.OrderDetailResponse;
import com.suryaenergi.sdm.backendapi.response.OrderListResponse;
import com.suryaenergi.sdm.backendapi.service.OrderService;
import com.suryaenergi.sdm.backendapi.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("month/{thn}/{bln}")
    public ResponseEntity<OrderListResponse> getOrderByBulan(@PathVariable int thn, int bln){
        OrderListResponse response = orderService.getListByMonth(thn, bln);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("day/{thn}/{bln}/{tgl}")
    public ResponseEntity<OrderListResponse> getOrderByTanggal(@PathVariable int thn, int bln, int tgl){
        OrderListResponse response = orderService.getListByTanggal(thn, bln,tgl);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("pemesan/{pid}")
    public ResponseEntity<OrderListResponse> getOrderByPemesan(@PathVariable String pid){
        OrderListResponse response = orderService.getListByPemesan(pid);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("{oid}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable String oid){
        OrderDetailResponse response = orderService.getOrderDetail(oid);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("na/{pid}")
    public ResponseEntity<OrderListResponse> getOrderNeedApprove(@PathVariable String pid){
        OrderListResponse response = orderService.getListNeedApprove(pid);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("app/{pid}")
    public ResponseEntity<OrderListResponse> getOrderApproved(@PathVariable String pid){
        OrderListResponse response = orderService.getListApproved(pid);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addOrderVehicle(@RequestBody OrderVehicleRequest req){
        MessageResponse response = orderService.addOrder(req);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<MessageResponse> updateOrderVehicle(@RequestBody OrderVehicleRequest req){
        MessageResponse response = orderService.updateOrder(req);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("{oid}")
    public ResponseEntity<MessageResponse> deleteOrderVehicle(@PathVariable String oid){
        MessageResponse response = orderService.deleteOrder(oid);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/approve")
    public ResponseEntity<MessageResponse> approveOrder(@RequestBody ApproveOrderRequest req){
        MessageResponse response = orderService.approveOrder(req, false);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/updatedriver")
    public ResponseEntity<MessageResponse> updateDriver(@RequestBody ApproveOrderRequest req){
        MessageResponse response = orderService.approveOrder(req, true);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @GetMapping("/appga")
    public ResponseEntity<OrderListResponse> getListApprovedGA(){
        OrderListResponse response = orderService.getListApprovedByGA();
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/done")
    public ResponseEntity<MessageResponse> doneOrder(@RequestBody OrderVehicleRequest req){
        MessageResponse response = orderService.doneOrder(req);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/approveGA")
    public ResponseEntity<MessageResponse> approveGA(@RequestBody ApproveOrderGARequest req){
        MessageResponse response = orderService.approveGA(req);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //rejectGA
    @PutMapping("/rejectGA")
    public ResponseEntity<MessageResponse> rejectGA(@RequestBody RejectOrderRequest req){
        MessageResponse response = orderService.rejectGA(req);
        if(response.getMsg().equalsIgnoreCase("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
