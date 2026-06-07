package com.program.passholder.Endpoints.Audit.GetLogs;
import java.sql.Date;

public class GetLogsDTO {
    public int pageNumber;
    public int rowsAmount;
    public String typeName;
    public String adminMail;
    public String ip;
    public Date fromDate;
    public Date toDate;
}
