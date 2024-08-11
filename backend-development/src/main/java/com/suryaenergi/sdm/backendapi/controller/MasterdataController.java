package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.entity.Daftarhadir;
import com.suryaenergi.sdm.backendapi.pojo.DataHariLibur;
import com.suryaenergi.sdm.backendapi.pojo.RuangMeetingData;
import com.suryaenergi.sdm.backendapi.repository.DaftarhadirRepository;
import com.suryaenergi.sdm.backendapi.request.*;
import com.suryaenergi.sdm.backendapi.response.*;
import com.suryaenergi.sdm.backendapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@RestController
@RequestMapping("master")
public class MasterdataController {
    @Autowired
    private HariliburService hariliburService;
    @Autowired
    private JamkerjaService jamkerjaService;
    @Autowired
    private DepartemenService departemenService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private LokasiAbsenService lokasiAbsenService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private IzinCutiService izinCutiService;
    @Autowired
    private RuangMeetingService ruangMeetingService;
    @Autowired
    private JatahCutiService jatahCutiService;

    @GetMapping("/harilibur/list/{no}/{size}")
    public ResponseEntity<HariliburListResponse> getListHarilibur(@PathVariable int no, @PathVariable int size)
    {
        HariliburListResponse response = hariliburService.getHariliburList(no,size);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/harilibur/{tahun}")
    public ResponseEntity<HariliburListResponse> getListHariliburByTahun(@PathVariable int tahun)
    {
        HariliburListResponse response = hariliburService.getHariliburListByTahun(tahun);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/harilibur")
    public ResponseEntity<HariLiburResponse> getHariLiburById(@RequestParam String id)
    {
        HariLiburResponse response = hariliburService.getHariLiburById(id);
        if(response.getMessage().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/dept/list/{no}/{size}")
    public ResponseEntity<DeptListResponse> getListDept(@PathVariable int no, @PathVariable int size)
    {
        DeptListResponse response = departemenService.getDeptList(no,size);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/dept")
    public ResponseEntity<MessageResponse> addDepartemen(@RequestBody DepartemenRequest req)
    {
        MessageResponse response = departemenService.addDepartemen(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/harilibur")
    public ResponseEntity<MessageResponse> updateHariLibur(@RequestBody HariLiburRequest req) {
        MessageResponse response = hariliburService.updateHariLibur(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/harilibur")
    public ResponseEntity<MessageResponse> deleteHariLibur(@RequestParam String id) {
        MessageResponse response = hariliburService.deleteHariLibur(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/harilibur")
    public ResponseEntity<MessageResponse> addHariLibur(@RequestBody HariLiburRequest req)
    {
        MessageResponse response = hariliburService.addHariLibur(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/jamkerja")
    public ResponseEntity<MessageResponse> addJamKerja(@RequestBody JamKerjaRequest req)
    {
        MessageResponse response = jamkerjaService.addJamKerja(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/vehicle/list")
    public ResponseEntity<VehicleListResponse> getListVehicle()
    {
        VehicleListResponse response = vehicleService.getVehicleList();
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/vehicle/{vehicle_id}")
    public ResponseEntity<VehicleResponse> getDetailVehicle(@PathVariable String vehicle_id){
        VehicleResponse response = vehicleService.getVehicleById(vehicle_id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/vehicle")
    public ResponseEntity<MessageResponse> addVehicle(@RequestBody VehicleRequest req)
    {
        MessageResponse response = vehicleService.addVehicle(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/vehicle")
    public ResponseEntity<MessageResponse> updateVehicle(@RequestBody VehicleUpdateRequest req)
    {
        MessageResponse response = vehicleService.updateVehicle(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/vehicle/{vehicle_id}")
    public ResponseEntity<MessageResponse> deleteVehicle(@PathVariable String vehicle_id)
    {
        MessageResponse response = vehicleService.deleteVehicle(vehicle_id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/lokasi")
    public ResponseEntity<MessageResponse> addLokasi(@RequestBody LokasiAbsenRequest req){
        MessageResponse response = lokasiAbsenService.addLokasi(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/lokasi")
    public ResponseEntity<LokasiAbsenListResponse> getLokasiList(){
        LokasiAbsenListResponse response = lokasiAbsenService.getLokasiList();
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/lokasi")
    public ResponseEntity<MessageResponse> updateLokasi(@RequestBody LokasiAbsenRequest req){
        MessageResponse response = lokasiAbsenService.updateLokasi(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } 
    }

    @DeleteMapping("/lokasi/{id}")
    public ResponseEntity<MessageResponse> deleteLokasi(@PathVariable Long id){
        MessageResponse response = lokasiAbsenService.deleteLokasi(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } 
    }

    @GetMapping("/lokasi/{id}")
    public ResponseEntity<LokasiAbsenResponse> getLokasiById(@PathVariable Long id){
        LokasiAbsenResponse response = lokasiAbsenService.getLokasiById(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } 
    }

    @GetMapping("role/{id}")
    public ResponseEntity<RoleDetailResponse> getRoleByRoleId(@PathVariable String id){
        RoleDetailResponse response = rolesService.getRoleById(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("role/list")
    public ResponseEntity<RoleListResponse> getAllRole(){
        RoleListResponse response = rolesService.getAllRole();
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("role")
    public ResponseEntity<RoleDetailResponse> addRole(@RequestBody RoleAddRequest req){
        RoleDetailResponse response = rolesService.addNewRole(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("role")
    public ResponseEntity<RoleDetailResponse> updateRole(@RequestBody RoleUpdateRequest req){
        RoleDetailResponse response = rolesService.updateRole(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("role/{id}")
    public ResponseEntity<MessageResponse> deleteRole(@PathVariable String id){
        MessageResponse response = rolesService.deleteRole(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/izincuti")
    public ResponseEntity<MessageResponse> addIzinCuti(@RequestBody JenisIzinCutiRequest req){
        MessageResponse response = izinCutiService.addJenisIzinCuti(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/izincuti/{id}")
    public ResponseEntity<IzinCutiResponse> findIzinCutiById(@PathVariable Long id){
        IzinCutiResponse response = izinCutiService.getJenisIzinCutiById(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } 
    }

    @PutMapping("/izincuti")
    public ResponseEntity<MessageResponse> updateIzinCuti(@RequestBody JenisIzinCutiRequest req){
        MessageResponse response = izinCutiService.updateJenisIzinCuti(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } 
    }

    @DeleteMapping("/izincuti/{id}")
    public ResponseEntity<MessageResponse> deleteIzinCuti(@PathVariable Long id){
        MessageResponse response = izinCutiService.deleteJenisIzinCuti(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } 
    }

    @GetMapping(value = "/jatahcuti/template_download")
    public ResponseEntity<byte[]> downloadJatahCutiTemplate(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(jatahCutiService.downloadTemplate());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(value = "/jatahcuti/upload_bulk")
    public ResponseEntity<MessageResponse> uploadJatahCutiTemplate(@RequestParam("file") MultipartFile file){
        try {
            jatahCutiService.loadTemplate(file.getBytes());
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(SUCCESS_MESSAGE));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/izincuti")
    public ResponseEntity<JenisIzinCutiListResponse> getAllIzinCuti(@RequestParam String empCode){
        JenisIzinCutiListResponse response = izinCutiService.getAllJenisIzinCuti(empCode);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/ruangmeeting/list")
    public ResponseEntity<RuangMeetingListResponse> getRuangMeetingList(){
        try {
            List<RuangMeetingData> data = ruangMeetingService.getAllRuangMeeting();
            return ResponseEntity.status(HttpStatus.OK).body(new RuangMeetingListResponse(SUCCESS_MESSAGE, data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RuangMeetingListResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/ruangmeeting")
    public ResponseEntity<MessageResponse> addRuangMeeting(@RequestParam String name, @RequestParam int capacity, @RequestParam String description) {
        try {
            ruangMeetingService.addRuangMeeting(new RuangMeetingData(0, name, capacity, description, null, null, null, null));
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(SUCCESS_MESSAGE));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/ruangmeeting/{id}")
    public ResponseEntity<RuangMeetingResponse> getRuangMeetingById(@PathVariable Long id) {
        try {
            RuangMeetingData data = ruangMeetingService.getRuangMeeting(id);
            return ResponseEntity.status(HttpStatus.OK).body(new RuangMeetingResponse(SUCCESS_MESSAGE, data.getId(), data.getName(), data.getCapacity(), data.getDescription(), data.getActiveEventId(), data.getActiveEventName()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RuangMeetingResponse(e.getMessage(), 0, "", 0, "", null, null));
        }
    }

    @PutMapping("/ruangmeeting")
    public ResponseEntity<MessageResponse> updateRuangMeeting(@RequestParam Long id, @RequestParam String name, @RequestParam int capacity, @RequestParam String description) {
        try {
            ruangMeetingService.updateRuangMeeting(new RuangMeetingData(id, name, capacity, description, null, null, null, null));
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(SUCCESS_MESSAGE));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/ruangmeeting/{id}")
    public ResponseEntity<MessageResponse> deleteRuangMeeting(@PathVariable Long id) {
        try {
            ruangMeetingService.deleteRuangMeeting(id);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(SUCCESS_MESSAGE));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }
}

