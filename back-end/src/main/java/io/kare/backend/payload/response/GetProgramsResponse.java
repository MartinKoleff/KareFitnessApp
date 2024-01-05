package io.kare.backend.payload.response;

import java.util.List;

public record GetProgramsResponse(List<GetProgramResponse> programs) {
}
