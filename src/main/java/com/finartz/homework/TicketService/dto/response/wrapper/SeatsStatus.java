package com.finartz.homework.TicketService.dto.response.wrapper;

import com.finartz.homework.TicketService.util.SeatStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeatsStatus {
    @ApiModelProperty(position = 1)
    private List<SeatDetail> takenSeats = new ArrayList<>();
    @ApiModelProperty(position = 2)
    private List<String> emptySeats = new ArrayList<>();

    public void sort(){
        Collections.sort(takenSeats, Comparator.comparing(o -> Integer.valueOf(o.getNo())));
        Collections.sort(emptySeats, Comparator.comparing(Integer::valueOf));
    }
}
