package io.kare.backend.controller;

import io.kare.backend.annotation.User;
import io.kare.backend.entity.UserEntity;
import io.kare.backend.service.ProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    public ResponseEntity<AddProgramResponse> addProgram(@RequestBody AddProgramRequest request, @User UserEntity user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.programService.addProgram(request, user));
    }

    public ResponseEntity<GetProgramResponse> getPrograms(@User UserEntity user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.programService.getPrograms(user));
    }

    public ResponseEntity<GetProgramResponse> getProgram(@RequestBody GetProgramRequest request, @User UserEntity user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.programService.getProgram(request, user));
    }
}
