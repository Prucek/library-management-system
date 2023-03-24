package cz.muni.fi.pa165.seminar3.reporting.service;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


/**
 * Represents report DTO which holds report info
 * @author Marek Miček
 */
@Getter
@Setter
public class ReportDto {

    private Instant generatedAt;

    private UserDto user;

    private int usersCount;

    private int borrowedBooksCount;

    private int returnedBooksCount;

    private int finesCount;

    private double finesTotalPaid;
}
