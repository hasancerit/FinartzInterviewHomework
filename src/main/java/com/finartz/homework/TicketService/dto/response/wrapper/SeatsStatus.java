package com.finartz.homework.TicketService.dto.response.wrapper;

import com.finartz.homework.TicketService.util.SeatStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeatsStatus {
    @ApiModelProperty(position = 1)
    private List<SeatDetail> takenSeats = new ArrayList<>();
    @ApiModelProperty(position = 2)
    private List<String> emptySeats = new ArrayList<>();


}
