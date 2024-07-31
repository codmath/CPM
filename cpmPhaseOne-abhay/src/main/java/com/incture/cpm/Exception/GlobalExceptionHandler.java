package com.incture.cpm.Exception;

//public class GlobalExceptionHandler {
//}
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestException(BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(value = InterviewerNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleInterviewerNotFoundException(InterviewerNotFoundException ex){
        JSONObject responseJson=new JSONObject();
        responseJson.put("status", false);
        responseJson.put("message", "Not able to find your id check it once");
        responseJson.put("statusCode", "404");
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json").body(responseJson.toString());
    }

    @ExceptionHandler(value = InterviewerSchedulingNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleInterviewerSchedulingNotFoundException(InterviewerSchedulingNotFoundException ex){
        JSONObject responseJson=new JSONObject();
        responseJson.put("status", false);
        responseJson.put("message", "Not able to find your id check it once");
        responseJson.put("statusCode", "404");
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json").body(responseJson.toString());
    }
    @ExceptionHandler(value = DuplicateEntryException.class)
    @ResponseBody
    public ResponseEntity<String> handleDuplicateEntryException(DuplicateEntryException ex){
        JSONObject responseJson=new JSONObject();
        responseJson.put("status", false);
        responseJson.put("message", "Duplicate entry for college");
        responseJson.put("statusCode", "404");
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("Content-Type", "application/json").body(responseJson.toString());
    }
}
