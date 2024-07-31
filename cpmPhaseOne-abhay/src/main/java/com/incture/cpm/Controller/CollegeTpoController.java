package com.incture.cpm.Controller;

import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.Service.CollegeTpoService;
import com.incture.cpm.Service.ExcelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class CollegeTpoController {
    @Autowired
    private CollegeTpoService myTpoService;
    @Autowired
    private ExcelService excelService;
    //Create a collegeTpo
    @PostMapping("/insertCollegeData")   //frontend me add CollegeData
    public ResponseEntity<CollegeTPO> insertCollData( @RequestBody CollegeTPO collegeTPO){

        return ResponseEntity.ok(myTpoService.insertFunction(collegeTPO));
   }
   //read the collegesTpo
    
//    @CrossOrigin(origins = "http://localhost:5173")
   @GetMapping("/viewData")
    public List<CollegeTPO> viewData(){
        return myTpoService.findData();
   }
    @GetMapping("/viewData/{collegeId}")
    public CollegeTPO getCollegeTPOById(@PathVariable int collegeId) {
        return myTpoService.getCollegeTPOById(collegeId);

    }
    //Update CollegeTpo
    @PutMapping("/updateData/{collegeId}")
    public ResponseEntity<CollegeTPO> updateCollegeTPO(@PathVariable int collegeId, @RequestBody CollegeTPO collegeTPO) {
        CollegeTPO updatedCollegeTPO = myTpoService.updateCollegeTPO(collegeId, collegeTPO);
        if (updatedCollegeTPO != null) {
            return ResponseEntity.ok(updatedCollegeTPO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //update by all fields
    //delete the CollegeTpo
    @DeleteMapping("/deleteData/{collegeId}")
    public void deleteCollegeTPO(@PathVariable int collegeId) {
        myTpoService.deleteCollegeTPO(collegeId);
    }
//    @DeleteMapping("/multipleDelete")
//    public String deleteEntities(@RequestBody List<Integer> ids) {
//        collegeTPORepo.deleteEntitiesByIdIn(ids);
//        return "Entities deleted successfully";
//    }
@DeleteMapping("/multipleDelete")
public String deleteEntities(@RequestBody List<Integer> ids) {
    for (Integer id : ids) {
        myTpoService.deleteCollegeTPO(id);
    }
    return "Entities deleted successfully";
}
 @PostMapping("/uploadcollegedata/excelsheet")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (excelService.hasExcelFormat(file)) {
            try {
                excelService.save(file);
                return ResponseEntity.status(HttpStatus.OK).body("File uploaded and data saved successfully!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Failed to upload and process file: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload an Excel file!");
    }

}
