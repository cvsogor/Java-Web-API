<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Batch Revenue/Payout update report</title>
</head>
<body>
    Offer ID: ${offerId}<br/>
    Media Source: ${mediaSource}<br/>
    Start Date: ${dateStart} <br/>
    End Date: ${dateEnd} <br/>
    Total records processed: ${totalRecords} <br/>
    Matched records: ${matchedRecords} <br/>
    Report Start: ${reportStart} <br/>
    Report End: ${reportEnd} <br/>
</body>
</html>