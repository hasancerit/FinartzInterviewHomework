package com.finartz.homework.TicketService.dto.response.wrapper;

import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeatDetail {
    @ApiModelProperty(position = 1)
    private String no;

    @ApiModelProperty(position = 2)
    private String ticketId;

    public SeatDetail(String no) {
        this.no = no;
    }
}
