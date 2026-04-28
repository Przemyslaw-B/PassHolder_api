package com.program.passholder.Endpoints.GetLogs;
import java.sql.Date;

public class GetLogsDTO {
    public int pageNumber;
    public int pageSize;
    public String typeName;
    public String userMail;
    public String adminMail;
    public Date fromDate;
    public Date toDate;
}
