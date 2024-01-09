package io.kare.backend.controller;

import io.kare.backend.annotation.User;
import io.kare.backend.entity.UserEntity;
import io.kare.backend.payload.request.*;
import io.kare.backend.payload.response.*;
import io.kare.backend.service.ProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping("/add")
    public ResponseEntity<AddProgramResponse> addProgram(@RequestBody AddProgramRequest request, @User UserEntity user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.programService.addProgram(request, user));
    }

    @GetMapping("/all")
    public ResponseEntity<GetProgramsResponse> getPrograms(@User UserEntity user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.programService.getPrograms(user));
    }

    @PostMapping("/get")
    public ResponseEntity<GetProgramResponse> getProgram(@RequestBody GetProgramRequest request, @User UserEntity user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.programService.getProgram(request, user));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateProgram(@RequestBody UpdateProgramRequest request, @User UserEntity user) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(this.programService.updateProgram(request, user));
    }
}
