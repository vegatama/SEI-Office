package com.suryaenergi.sdm.backendapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.suryaenergi.sdm.backendapi.config.JwtService;
import com.suryaenergi.sdm.backendapi.entity.*;
import com.suryaenergi.sdm.backendapi.pojo.*;
import com.suryaenergi.sdm.backendapi.repository.*;
import com.suryaenergi.sdm.backendapi.request.*;
import com.suryaenergi.sdm.backendapi.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtService jwtUtils;
    @Autowired
    private KetidakhadiranRepository ketidakhadiranRepository;
    @Autowired
    private HariliburRepository hariliburRepository;
    @Autowired
    private LokasiAbsenRepository lokasiAbsenRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private SiteOptionsRepository siteOptionsRepository;
    @Autowired
    private AmazonS3 s3;

    public UserLoginResponse verifyUser(UserLoginRequest req) {
        UserLoginResponse res = new UserLoginResponse();
        try{
            Employee employee = employeeRepository.findByEmail(req.getEmail());
            if (employee == null) {
                res.setMsg("EMAIL NOT FOUND");
            } else {
                SiteOptions maintenance = siteOptionsRepository.findByName("maintenance_mode");
                if(bCryptPasswordEncoder.matches(req.getPassword(), employee.getPassword())){
                    res.setMsg("SUCCESS");
                    res.setName(employee.getFullName());
                    res.setEmail(employee.getEmail());
                    res.setEmployee_code(employee.getEmployeeCode());
                    res.setNik(employee.getNik());
                    String token = jwtUtils.generateToken(employee);
                    res.setToken(token);
                    res.setAccess(employee.getAccess());
                    res.setCreate_password(false);
                    res.setStatus(employee.getStatus());
                    res.set_active(employee.isActive());
                    res.setEmployee_id(employee.getEmployeeId());
                    res.setJobTitle(employee.getJobTitle());
                    res.setAvatar(employee.getAvatar());

                    if(maintenance != null)
                        res.setMaintenance_mode(maintenance.getValue());

                    LokasiAbsen lokasi;
                    if(employee.getLokasiAbsenId() != null) {
                        lokasi = employee.getLokasiAbsenId();
                    }
                    else{
                        lokasi = lokasiAbsenRepository.findFirstByIsDefault(true);
                        employee.setLokasiAbsenId(lokasi);
                    }
                    LokasiAbsenData lokasiAbsenData = new LokasiAbsenData();
                    lokasiAbsenData.setId(lokasi.getId());
                    lokasiAbsenData.setLokasi_absen(lokasi.getLokasiAbsen());
                    lokasiAbsenData.setLatitude(lokasi.getLatitude());
                    lokasiAbsenData.setLongitude(lokasi.getLongitude());
                    res.setLokasi_absen(lokasiAbsenData);

                    res.setNav_id(employee.getUserIdNav());

                    res.setAvatar(employee.getAvatar());

                    //set last login
                    employee.setLastLogin(LocalDateTime.now());
                    employeeRepository.save(employee);

                    //get roles
                    Roles userRoles;
                    if(employee.getRoles() != null) {
                        userRoles = employee.getRoles();
                    }
                    else{
                        userRoles = rolesRepository.findByIsDefault(true);
                    }
                    RoleData roleData = new RoleData();
                    roleData.setId(userRoles.getRoleId());
                    roleData.setName(userRoles.getRoleName());
                    roleData.setDescription(userRoles.getRoleDescription());
                    roleData.setPermission(userRoles.getPermission());
                    res.setRoles(roleData);
                }
                else{
                    res.setMsg("LOGIN INVALID");
                }
            }
        }catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public EmployeeResponse addEmployee(EmployeeAddRequest req) {
        EmployeeResponse res = new EmployeeResponse();
        try{
            Employee employee = employeeRepository.findByEmail(req.getEmail());
            if(employee != null)
                throw new Exception("EMAIL ALREADY EXIST");

            employee = employeeRepository.findByEmployeeCode(req.getEmployee_code());
            if(employee != null)
                throw new Exception("EMPLOYEE CODE ALREADY EXIST");

            employee = employeeRepository.findByNik(req.getNik());
            if(employee != null)
                throw new Exception("NIK ALREADY EXIST");

            employee.setEmployeeId(UUID.randomUUID().toString());

            Employee cekid = employeeRepository.findByEmployeeId(employee.getEmployeeId());
            while(cekid != null){
                employee.setEmployeeId(UUID.randomUUID().toString());
                cekid = employeeRepository.findByEmployeeId(employee.getEmployeeId());
            }

            employee = new Employee();
            employee.setEmployeeCode(req.getEmployee_code());
            employee.setEmail(req.getEmail());
            employee.setNik(req.getNik());
            employee.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
            employee.setPassword(req.getPassword());
            employee.setDirektorat(req.getDirektorat());
            employee.setFullName(req.getName());
            employee.setAtasanUserId(req.getAtasan_user_id());
            employee.setBagianFungsi(req.getBagian_fungsi());
            employee.setGolongan(req.getGolongan());
            employee.setGrade(Long.valueOf(req.getGrade()));
            employee.setKeterangan(req.getKeterangan());
            employee.setPersonIdMesinAbsen(req.getPerson_id_mesin_absen());
            employee.setMobilePhoneNo(req.getMobile_phone_no());
            employee.setPhoneNo(req.getPhone_no());
            employee.setJobTitle(req.getJob_title());
            employee.setSisaCuti(Long.valueOf(req.getSisa_cuti()));

            employeeRepository.save(employee);

            res.setMsg("SUCCESS");
            res.setEmployee_code(employee.getEmployeeCode());
            res.setNik(employee.getNik());
            res.setEmail(employee.getEmail());
            res.setFull_name(employee.getFullName());
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public MessageResponse deploySeed() {
        MessageResponse res = new MessageResponse();

        try{
            Employee employee = new Employee();
            employee.setEmployeeId(UUID.randomUUID().toString());

            Employee cekid = employeeRepository.findByEmployeeId(employee.getEmployeeId());
            while(cekid != null){
                employee.setEmployeeId(UUID.randomUUID().toString());
                cekid = employeeRepository.findByEmployeeId(employee.getEmployeeId());
            }

            employee.setEmployeeCode("EMP999");
            employee.setEmail("fauzan.azmi@suryaenergi.com");
            employee.setNik("1306020");
            employee.setPassword(bCryptPasswordEncoder.encode("paswot"));
            employee.setPersonIdMesinAbsen("'286");
            employee.setBagianFungsi("Bagian SDM & Umum");
            employee.setFullName("Fauzan Azmi");
            employee.setGrade(0L);
            employee.setThp((double) 0);

            employeeRepository.save(employee);
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public EmployeeDetailResponse getEmployeeDetail(String id) {
        EmployeeDetailResponse res = new EmployeeDetailResponse();
        try{
            Employee employee = employeeRepository.findByEmployeeId(id);
            if(employee == null)
                throw new Exception("EMPLOYEE NOT FOUND");

            res.setMsg("SUCCESS");
            res.setEmployee_code(employee.getEmployeeCode());
            res.setEmail(employee.getEmail());
            res.setNik(employee.getNik());
            res.setName(employee.getFullName());
            res.setGrade(employee.getGrade());
            res.setBagian_fungsi(employee.getBagianFungsi());
            res.setDirektorat(employee.getDirektorat());
            res.setGolongan(employee.getGolongan());
            res.setAtasan_user_id(employee.getAtasanUserId());
            res.setJob_title(employee.getJobTitle());
            res.setKeterangan(employee.getKeterangan());
            res.setUnit_kerja(employee.getUnitKerja());
            res.setMobile_phone_no(employee.getMobilePhoneNo());
            res.setPhone_no(employee.getPhoneNo());
            res.setPerson_id_mesin_absen(employee.getPersonIdMesinAbsen());
            res.setSisa_cuti(employee.getSisaCuti());
            res.setThp(employee.getThp());
            res.setAvatar(employee.getAvatar());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            res.setBirthday(employee.getTanggalLahir().format(formatter));

            res.setFirst_name(employee.getFirstName());
            res.setMiddle_name(employee.getMiddleName());
            res.setLast_name(employee.getLastName());
            res.setGender(employee.getGender());
            res.setPlace_birthdate(employee.getBirthPlace());
            res.setReligion(employee.getReligion());
            res.setAddress(employee.getAddress());
            res.setPost_code(employee.getPostCode());
            res.setCity(employee.getCity());
            res.setGol_darah(employee.getGolonganDarah());
            res.setStatus_pernikahan(employee.getStatusPernikahan());
            res.setNpwp(employee.getNpwp());

            res.setNo_telp_darurat(employee.getTelpDarurat());
            res.setStatus_darurat(employee.getStatusTelpDarurat());

            res.setPendidikan_terakhir(employee.getPendidikanTerakhir());
            res.setJurusan_pendidikan(employee.getJurusanPendidikan());
            res.setSpesialis_pendidikan(employee.getSpesialisPendidikan());
            res.setInstitusi_pendidikan(employee.getInstitusiPendidikan());

            res.setFacebook(employee.getFacebook());
            res.setTwitter(employee.getTwitter());
            res.setInstagram(employee.getInstagram());

            res.setJumlah_keluarga(employee.getAnggotaKeluarga());
            if(employee.getNamaIstriSuami() != null)
                res.setNama_istri_suami(employee.getNamaIstriSuami());
            if(employee.getTglLahirIstriSuami() != null)
                res.setDob_istri_suami(employee.getTglLahirIstriSuami().format(formatter));
            if(employee.getNamaAnak1() != null)
                res.setNama_anak_1(employee.getNamaAnak1());
            if(employee.getTglLahirAnak1() != null)
                res.setDob_anak_1(employee.getTglLahirAnak1().format(formatter));
            if(employee.getNamaAnak2() != null)
                res.setNama_anak_2(employee.getNamaAnak2());
            if(employee.getTglLahirAnak2() != null)
                res.setDob_anak_2(employee.getTglLahirAnak2().format(formatter));
            if(employee.getNamaAnak3() != null)
                res.setNama_anak_3(employee.getNamaAnak3());
            if(employee.getTglLahirAnak3() != null)
                res.setDob_anak_3(employee.getTglLahirAnak3().format(formatter));
            if(employee.getNamaAnak4() != null)
                res.setNama_anak_4(employee.getNamaAnak1());
            if(employee.getTglLahirAnak4() != null)
                res.setDob_anak_4(employee.getTglLahirAnak4().format(formatter));
            if(employee.getNamaAnak5() != null)
                res.setNama_anak_5(employee.getNamaAnak1());
            if(employee.getTglLahirAnak5() != null)
                res.setDob_anak_5(employee.getTglLahirAnak5().format(formatter));

            res.setBank_branch(employee.getBankBranch());
            res.setBank_account(employee.getBankAccount());
            res.setBank_branch2(employee.getBankBranch2());
            res.setBank_account2(employee.getBankAccount2());
            res.setUser_id_nav(employee.getUserIdNav());
            res.setAtasan_user_id(employee.getAtasanUserId());
            res.setCompany_code(employee.getCompanyCode());
            res.setJabatan(employee.getJabatan());
            res.setKlasifikasi_level_jabatan(employee.getKlasifikasiLevelJabatan());
            res.setJob_stream(employee.getJobstream());
            res.setKlasifikasi_job(employee.getKlasifikasiJob());
            res.setRumpun_jabatan(employee.getRumpunJabatan());
            res.setLokasi_kerja(employee.getLokasiKerja());
            res.setStatus_kerja(employee.getStatus());
            if(employee.getMulaiKerja() != null) {
                res.setMulai_kerja(employee.getMulaiKerja().format(formatter));
            }
            if(employee.getAkhirKerja() != null) {
                res.setAkhir_kerja(employee.getAkhirKerja().format(formatter));
            }

            res.setAvatar(employee.getAvatar());
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public EmployeeListResponse getEmployeeList(int no, int size) {
        EmployeeListResponse res = new EmployeeListResponse();

        List<DataEmployee> dataEmployeeList = new ArrayList<>();
        Pageable paging = PageRequest.of(no,size);
        try {
            Page<Employee> employees = employeeRepository.findByStatusOrStatusOrStatusOrStatusOrderByFullNameAsc("Cakara (Calon Karyawan)","Karyawan Tetap","KWT (Kerja Waktu Tertentu)","THL (Tenaga Harian Lepas)",paging);
            if(employees.hasContent()){
                for(Employee employee:employees){
                    DataEmployee dataEmployee = new DataEmployee();
                    dataEmployee.setEmail(employee.getEmail());
                    dataEmployee.setEmployee_code(employee.getEmployeeCode());
                    dataEmployee.setId(employee.getEmployeeId());
                    dataEmployee.setNik(employee.getNik());
                    dataEmployee.setDirektorat(employee.getDirektorat());
                    dataEmployee.setName(employee.getFullName());
                    dataEmployee.setThp(employee.getThp());
                    dataEmployee.setJob_title(employee.getJobTitle());
                    dataEmployee.setUnit_kerja(employee.getUnitKerja());
                    dataEmployee.setBagian_fungsi(employee.getBagianFungsi());
                    dataEmployee.setSisa_cuti(employee.getSisaCuti());
                    dataEmployee.setStatus(employee.getStatus());
                    LokasiAbsen lokasi = employee.getLokasiAbsenId();
                    if (lokasi == null) {
                        lokasi = lokasiAbsenRepository.findFirstByIsDefault(true);
                    }
                    dataEmployee.setLokasi_absen(lokasi.getLokasiAbsen());
                    dataEmployeeList.add(dataEmployee);
                }
                res.setCount(dataEmployeeList.size());
                res.setMsg("SUCCESS");
                res.setData(dataEmployeeList);
            }
            else {
                res.setMsg("SUCCESS");
                res.setCount(0);
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public UserLoginResponse verifyUserGoogle(UserLoginGoogleRequest req) {
        UserLoginResponse res = new UserLoginResponse();
        try{
            String uri = "https://www.googleapis.com/oauth2/v1/userinfo?access_token="+req.getToken();
            RestTemplate restTemplate = new RestTemplate();
            GoogleResponse googleResponse = restTemplate.getForObject(uri, GoogleResponse.class);

            Employee employee = employeeRepository.findByEmail(googleResponse.getEmail());
            SiteOptions maintenance = siteOptionsRepository.findByName("maintenance_mode");
            if (employee == null) {
                res.setMsg("EMAIL NOT FOUND");
            } else {
                res.setMsg("SUCCESS");
                res.setName(employee.getFullName());
                res.setEmail(employee.getEmail());
                res.setEmployee_code(employee.getEmployeeCode());
                res.setNik(employee.getNik());
                String token = jwtUtils.generateToken(employee);
                res.setToken(token);
                res.setAccess(employee.getAccess());
                res.setStatus(employee.getStatus());
                res.set_active(employee.isActive());
                res.setEmployee_id(employee.getEmployeeId());
                res.setJobTitle(employee.getJobTitle());

                LokasiAbsen lokasi;
                if(employee.getLokasiAbsenId() != null) {
                    lokasi = employee.getLokasiAbsenId();
                }
                else{
                    lokasi = lokasiAbsenRepository.findFirstByIsDefault(true);
                }
                LokasiAbsenData lokasiAbsenData = new LokasiAbsenData();
                lokasiAbsenData.setId(lokasi.getId());
                lokasiAbsenData.setLokasi_absen(lokasi.getLokasiAbsen());
                lokasiAbsenData.setLatitude(lokasi.getLatitude());
                lokasiAbsenData.setLongitude(lokasi.getLongitude());
                res.setLokasi_absen(lokasiAbsenData);
                
                res.setNav_id(employee.getUserIdNav());
                if(employee.getPassword() == null)
                    res.setCreate_password(true);
                else
                    res.setCreate_password(false);

                res.setAvatar(employee.getAvatar());

                res.setAvatar(employee.getAvatar());

                if(maintenance != null)
                    res.setMaintenance_mode(maintenance.getValue());

                //set last login
                employee.setLastLogin(LocalDateTime.now());
                employeeRepository.save(employee);

                //get roles
                Roles userRoles;
                if(employee.getRoles() != null) {
                    userRoles = employee.getRoles();
                }
                else{
                    userRoles = rolesRepository.findByIsDefault(true);
                }
                RoleData roleData = new RoleData();
                roleData.setId(userRoles.getRoleId());
                roleData.setName(userRoles.getRoleName());
                roleData.setDescription(userRoles.getRoleDescription());
                roleData.setPermission(userRoles.getPermission());
                res.setRoles(roleData);
            }
        }catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public MessageResponse createPassword(CreatePasswordRequest req) {
        MessageResponse res = new MessageResponse();

        try{
            Employee employee = employeeRepository.findByEmployeeCode(req.getEmpcode());
            if(employee == null)
                throw new Exception("DATA NOT FOUND");

            employee.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
            employeeRepository.save(employee);

            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse changePassword(ChangePasswordRequest req) {
        MessageResponse res = new MessageResponse();

        try{
            Employee employee = employeeRepository.findByEmployeeCode(req.getEmpcode());
            if(employee == null)
                throw new Exception("DATA NOT FOUND");

            if(!bCryptPasswordEncoder.matches(req.getOld_password(), employee.getPassword()))
                throw new Exception("WRONG OLD PASSWORD");

            employee.setPassword(bCryptPasswordEncoder.encode(req.getNew_password()));
            employeeRepository.save(employee);

            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public EmployeeListResponse getThlList() {
        EmployeeListResponse res = new EmployeeListResponse();

        List<DataEmployee> dataEmployeeList = new ArrayList<>();
        List<Employee> employeeList = employeeRepository.findAllByStatus("THL (Tenaga Harian Lepas)");
        try{
            if(employeeList.size() > 0){
                for(Employee employee:employeeList){
                    DataEmployee dataEmployee = new DataEmployee();
                    dataEmployee.setEmail(employee.getEmail());
                    dataEmployee.setEmployee_code(employee.getEmployeeCode());
                    dataEmployee.setId(employee.getEmployeeId());
                    dataEmployee.setNik(employee.getNik());
                    dataEmployee.setDirektorat(employee.getDirektorat());
                    dataEmployee.setName(employee.getFullName());
                    dataEmployee.setThp(employee.getThp());
                    dataEmployee.setJob_title(employee.getJobTitle());
                    dataEmployee.setUnit_kerja(employee.getUnitKerja());
                    dataEmployee.setBagian_fungsi(employee.getBagianFungsi());
                    dataEmployee.setSisa_cuti(employee.getSisaCuti());
                    dataEmployee.setStatus(employee.getStatus());
                    dataEmployeeList.add(dataEmployee);
                }
                res.setCount(dataEmployeeList.size());
                res.setMsg("SUCCESS");
                res.setData(dataEmployeeList);
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public EmployeeCutiListResponse getCutiList(int year, int month) {
        EmployeeCutiListResponse res = new EmployeeCutiListResponse();

        try{
            List<DataCutiEmployee> dataCutiEmployeeList = new ArrayList<>();
            List<Employee> employeeList = employeeRepository.findAllByStatusOrStatusOrStatus("Cakara (Calon Karyawan)","Karyawan Tetap","KWT (Kerja Waktu Tertentu)");
            for(Employee employee: employeeList){
                List<Ketidakhadiran> ketidakhadiranList = ketidakhadiranRepository.findAllCutiByEmployeeCodeYearMonth(employee.getEmployeeCode(),year,month);
                String tglCuti = "";
                int jumCuti = 0;
                if(ketidakhadiranList.size() > 0){
                    for(Ketidakhadiran ketidakhadiran:ketidakhadiranList){
                        String tgl = ketidakhadiran.getTanggal().format(DateTimeFormatter.ofPattern("d MMM YYYY"));
                        ZoneId defaultZoneId = ZoneId.systemDefault();
                        Date tmpdate = Date.from(ketidakhadiran.getTanggal().atStartOfDay(defaultZoneId).toInstant());
                        Format f = new SimpleDateFormat("EEEE");
                        String nmhari = f.format(tmpdate);
                        HariLibur hariLibur = hariliburRepository.findByTanggal(ketidakhadiran.getTanggal());

                        if (!nmhari.equals("Saturday") && !nmhari.equals("Sunday") && hariLibur == null) {
                            tglCuti = tglCuti + tgl + ", ";
                            jumCuti++;
                        }
                    }

                    if(!tglCuti.equals("")) {
                        tglCuti = tglCuti.substring(0, tglCuti.length() - 2);
                    }

                    DataCutiEmployee dataCutiEmployee = new DataCutiEmployee();
                    dataCutiEmployee.setEmployee_code(employee.getEmployeeCode());
                    dataCutiEmployee.setNik(employee.getNik());
                    dataCutiEmployee.setName(employee.getFullName());
                    dataCutiEmployee.setSisa_cuti(employee.getSisaCuti().toString());
                    dataCutiEmployee.setBagian_fungsi(employee.getBagianFungsi());
                    dataCutiEmployee.setTgl_cuti(tglCuti);
                    dataCutiEmployee.setJumlah_cuti(jumCuti);
                    dataCutiEmployeeList.add(dataCutiEmployee);
                }

            }
            res.setMsg("SUCCESS");
            res.setCount(dataCutiEmployeeList.size());
            res.setData(dataCutiEmployeeList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public CutiDetilResponse getCutiDetil(AbsenSayaRequest req) {
        CutiDetilResponse res = new CutiDetilResponse();
        List<DetilCuti> detilCutiList = new ArrayList<>();

        try{
            List<Ketidakhadiran> ketidakhadiranList = ketidakhadiranRepository.findAllCutiByEmployeeCodeYearMonth(req.getEmployee_code(), req.getYear(), req.getMonth());
            if(ketidakhadiranList.size() > 0){
                for(Ketidakhadiran ketidakhadiran:ketidakhadiranList) {
                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    Date tmpdate = Date.from(ketidakhadiran.getTanggal().atStartOfDay(defaultZoneId).toInstant());
                    Format f = new SimpleDateFormat("EEEE");
                    String nmhari = f.format(tmpdate);
                    HariLibur hariLibur = hariliburRepository.findByTanggal(ketidakhadiran.getTanggal());

                    if (!nmhari.equals("Saturday") && !nmhari.equals("Sunday") && hariLibur == null) {
                        DetilCuti detilCuti = new DetilCuti();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        detilCuti.setTgl(ketidakhadiran.getTanggal().format(formatter));
                        detilCuti.setKeterangan(ketidakhadiran.getDescription());
                        detilCutiList.add(detilCuti);
                    }
                }
            }
            Employee employee = employeeRepository.findByEmployeeCode(req.getEmployee_code());
            res.setMsg("SUCCESS");
            res.setEmployee_code(employee.getEmployeeCode());
            res.setName(employee.getFullName());
            res.setJumlah_data(detilCutiList.size());
            res.setData(detilCutiList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public BirthdayResponse getBirthdayList() {
        BirthdayResponse res = new BirthdayResponse();
        List<EmployeeBirthday> employeeBirthdays = new ArrayList<>();

        try{
            List<Employee> employeeList = employeeRepository.findAllByStatusOrStatusOrStatusOrStatus("Cakara (Calon Karyawan)","Karyawan Tetap","KWT (Kerja Waktu Tertentu)","THL (Tenaga Harian Lepas)");
            if(employeeList.size() < 0)
                throw new Exception("NO DATA");

            for(Employee employee: employeeList){
                EmployeeBirthday employeeBirthday = new EmployeeBirthday();
                employeeBirthday.setName(employee.getFullName());
                if(employee.getTanggalLahir() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    employeeBirthday.setBirthday(employee.getTanggalLahir().format(formatter));
                }
                employeeBirthdays.add(employeeBirthday);
            }

            res.setMsg("SUCCESS");
            res.setCount(employeeList.size());
            res.setData(employeeBirthdays);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public EmployeeDetailResponse convert(Employee employee) {
        EmployeeDetailResponse res = new EmployeeDetailResponse();
        res.setMsg("SUCCESS");
        res.setEmployee_code(employee.getEmployeeCode());
        res.setEmail(employee.getEmail());
        res.setNik(employee.getNik());
        res.setName(employee.getFullName());
        res.setGrade(employee.getGrade());
        res.setBagian_fungsi(employee.getBagianFungsi());
        res.setDirektorat(employee.getDirektorat());
        res.setGolongan(employee.getGolongan());
        res.setAtasan_user_id(employee.getAtasanUserId());
        res.setJob_title(employee.getJobTitle());
        res.setKeterangan(employee.getKeterangan());
        res.setUnit_kerja(employee.getUnitKerja());
        res.setMobile_phone_no(employee.getMobilePhoneNo());
        res.setPhone_no(employee.getPhoneNo());
        res.setPerson_id_mesin_absen(employee.getPersonIdMesinAbsen());
        res.setSisa_cuti(employee.getSisaCuti());
        res.setThp(employee.getThp());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        res.setBirthday(employee.getTanggalLahir().format(formatter));

        res.setFirst_name(employee.getFirstName());
        res.setMiddle_name(employee.getMiddleName());
        res.setLast_name(employee.getLastName());
        res.setGender(employee.getGender());
        res.setPlace_birthdate(employee.getBirthPlace());
        res.setReligion(employee.getReligion());
        res.setAddress(employee.getAddress());
        res.setPost_code(employee.getPostCode());
        res.setCity(employee.getCity());
        res.setGol_darah(employee.getGolonganDarah());
        res.setStatus_pernikahan(employee.getStatusPernikahan());
        res.setNpwp(employee.getNpwp());

        res.setNo_telp_darurat(employee.getTelpDarurat());
        res.setStatus_darurat(employee.getStatusTelpDarurat());

        res.setPendidikan_terakhir(employee.getPendidikanTerakhir());
        res.setJurusan_pendidikan(employee.getJurusanPendidikan());
        res.setSpesialis_pendidikan(employee.getSpesialisPendidikan());
        res.setInstitusi_pendidikan(employee.getInstitusiPendidikan());

        res.setFacebook(employee.getFacebook());
        res.setTwitter(employee.getTwitter());
        res.setInstagram(employee.getInstagram());

        res.setAvatar(employee.getAvatar());

        res.setJumlah_keluarga(employee.getAnggotaKeluarga());
        if(employee.getNamaIstriSuami() != null)
            res.setNama_istri_suami(employee.getNamaIstriSuami());
        if(employee.getTglLahirIstriSuami() != null)
            res.setDob_istri_suami(employee.getTglLahirIstriSuami().format(formatter));
        if(employee.getNamaAnak1() != null)
            res.setNama_anak_1(employee.getNamaAnak1());
        if(employee.getTglLahirAnak1() != null)
            res.setDob_anak_1(employee.getTglLahirAnak1().format(formatter));
        if(employee.getNamaAnak2() != null)
            res.setNama_anak_2(employee.getNamaAnak2());
        if(employee.getTglLahirAnak2() != null)
            res.setDob_anak_2(employee.getTglLahirAnak2().format(formatter));
        if(employee.getNamaAnak3() != null)
            res.setNama_anak_3(employee.getNamaAnak3());
        if(employee.getTglLahirAnak3() != null)
            res.setDob_anak_3(employee.getTglLahirAnak3().format(formatter));
        if(employee.getNamaAnak4() != null)
            res.setNama_anak_4(employee.getNamaAnak1());
        if(employee.getTglLahirAnak4() != null)
            res.setDob_anak_4(employee.getTglLahirAnak4().format(formatter));
        if(employee.getNamaAnak5() != null)
            res.setNama_anak_5(employee.getNamaAnak1());
        if(employee.getTglLahirAnak5() != null)
            res.setDob_anak_5(employee.getTglLahirAnak5().format(formatter));

        res.setBank_branch(employee.getBankBranch());
        res.setBank_account(employee.getBankAccount());
        res.setBank_branch2(employee.getBankBranch2());
        res.setBank_account2(employee.getBankAccount2());
        res.setUser_id_nav(employee.getUserIdNav());
        res.setAtasan_user_id(employee.getAtasanUserId());
        res.setCompany_code(employee.getCompanyCode());
        res.setJabatan(employee.getJabatan());
        res.setKlasifikasi_level_jabatan(employee.getKlasifikasiLevelJabatan());
        res.setJob_stream(employee.getJobstream());
        res.setKlasifikasi_job(employee.getKlasifikasiJob());
        res.setRumpun_jabatan(employee.getRumpunJabatan());
        res.setLokasi_kerja(employee.getLokasiKerja());
        res.setStatus_kerja(employee.getStatus());
        if(employee.getMulaiKerja() != null) {
            res.setMulai_kerja(employee.getMulaiKerja().format(formatter));
        }
        if(employee.getAkhirKerja() != null) {
            res.setAkhir_kerja(employee.getAkhirKerja().format(formatter));
        }
        return res;
    }

    public EmployeeDetailResponse getEmployeeDetailByCode(String id) {
        try{
            Employee employee = employeeRepository.findByEmployeeCode(id);
            if(employee == null)
                throw new Exception("EMPLOYEE NOT FOUND");
            return convert(employee);
        }
        catch (Exception ex){
            EmployeeDetailResponse res = new EmployeeDetailResponse();
            res.setMsg("ERROR: "+ex.getMessage());
            return res;
        }
    }

    public EmpDashboardResponse getDashboardData() {
        EmpDashboardResponse res = new EmpDashboardResponse();

        try{
            List<Employee> employeeListTetap = employeeRepository.findAllByStatus("Karyawan Tetap");
            List<Employee> employeeListKwt = employeeRepository.findAllByStatus("KWT (Kerja Waktu Tertentu)");
            List<Employee> employeeListThl = employeeRepository.findAllByStatus("THL (Tenaga Harian Lepas)");
            int totalemp = employeeListTetap.size() + employeeListKwt.size() + employeeListThl.size();

            res.setMsg("SUCCESS");
            res.setEmp_tetap(employeeListTetap.size());
            res.setEmp_kwt(employeeListKwt.size());
            res.setEmp_thl(employeeListThl.size());
            res.setEmp_total(totalemp);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public EmployeeListResponse getEmployeeListWithStatus(EmployeeListRequest req) {
        EmployeeListResponse res = new EmployeeListResponse();

        List<DataEmployee> dataEmployeeList = new ArrayList<>();
        try{
            List<Employee> employeeList = new ArrayList<>();
            if(req.getStatus().equals("")){
                employeeList = employeeRepository.findAllByStatusOrStatusOrStatus("Karyawan Tetap","KWT (Kerja Waktu Tertentu)","THL (Tenaga Harian Lepas)");
            }
            else{
                employeeList = employeeRepository.findAllByStatus(req.getStatus());
            }
            if(employeeList.size() > 0){
                for(Employee employee:employeeList){
                    DataEmployee dataEmployee = new DataEmployee();
                    dataEmployee.setEmail(employee.getEmail());
                    dataEmployee.setEmployee_code(employee.getEmployeeCode());
                    dataEmployee.setId(employee.getEmployeeId());
                    dataEmployee.setNik(employee.getNik());
                    dataEmployee.setDirektorat(employee.getDirektorat());
                    dataEmployee.setName(employee.getFullName());
                    dataEmployee.setThp(employee.getThp());
                    dataEmployee.setJob_title(employee.getJobTitle());
                    dataEmployee.setUnit_kerja(employee.getUnitKerja());
                    dataEmployee.setBagian_fungsi(employee.getBagianFungsi());
                    dataEmployee.setSisa_cuti(employee.getSisaCuti());
                    dataEmployee.setStatus(employee.getStatus());
                    dataEmployeeList.add(dataEmployee);
                }
                res.setCount(dataEmployeeList.size());
                res.setMsg("SUCCESS");
                res.setData(dataEmployeeList);
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public EmployeeListResponse getAllEmployee() {
        EmployeeListResponse res = new EmployeeListResponse();

        List<DataEmployee> dataEmployeeList = new ArrayList<>();
        Pageable paging = PageRequest.of(0,999999999);
        try {
            Page<Employee> employees = employeeRepository.findByStatusOrStatusOrStatusOrStatusOrderByFullNameAsc("Cakara (Calon Karyawan)","Karyawan Tetap","KWT (Kerja Waktu Tertentu)","THL (Tenaga Harian Lepas)",paging);
            if(employees.hasContent()){
                for(Employee employee:employees){
                    DataEmployee dataEmployee = new DataEmployee();
                    dataEmployee.setEmail(employee.getEmail());
                    dataEmployee.setEmployee_code(employee.getEmployeeCode());
                    dataEmployee.setId(employee.getEmployeeId());
                    dataEmployee.setNik(employee.getNik());
                    dataEmployee.setDirektorat(employee.getDirektorat());
                    dataEmployee.setName(employee.getFullName());
                    dataEmployee.setThp(employee.getThp());
                    dataEmployee.setJob_title(employee.getJobTitle());
                    dataEmployee.setUnit_kerja(employee.getUnitKerja());
                    dataEmployee.setBagian_fungsi(employee.getBagianFungsi());
                    dataEmployee.setSisa_cuti(employee.getSisaCuti());
                    dataEmployee.setStatus(employee.getStatus());
                    dataEmployeeList.add(dataEmployee);
                }
                res.setCount(dataEmployeeList.size());
                res.setMsg("SUCCESS");
                res.setData(dataEmployeeList);
            }
            else {
                res.setMsg("SUCCESS");
                res.setCount(0);
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public UpdateLokasiAbsenResponse updateLokasiAbsen(UpdateLokasiAbsenRequest req){
        UpdateLokasiAbsenResponse res = new UpdateLokasiAbsenResponse();
        try {
            Employee employee = employeeRepository.findByEmployeeCode(req.getEmployee_code());
            if (employee == null) {
                throw new Exception("USER NOT FOUND");
            }
            LokasiAbsen lokasi = lokasiAbsenRepository.findLokasiById(req.getLokasi_absen_id());
            if (lokasi == null) {
                throw new Exception("LOKASI NOT FOUND");
            }
            String bagian = employee.getBagianFungsi();
            String name = employee.getFirstName();
            employee.setLokasiAbsenId(lokasi);
            employeeRepository.save(employee);
            res.setMsg("SUCCESS");
            res.setName(name);
            res.setBagian(bagian);
            res.setLokasi_absen_id(lokasi.getId());
            res.setLokasi_absen(lokasi.getLokasiAbsen());
        
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public EmployeeDetailResponse getEmployeeDetailByNavId(String id) {
        try {
            Employee employee = employeeRepository.findFirstByUserIdNav(id);
            if (employee == null) {
                throw new Exception("EMPLOYEE NOT FOUND");
            }
            return convert(employee);
        } catch (Exception ex) {
            EmployeeDetailResponse res = new EmployeeDetailResponse();
            res.setMsg("ERROR: "+ex.getMessage());
            return res;
        }
    }
}
