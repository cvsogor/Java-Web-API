<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Aditor BI</title>
</head>
<body>
<!--
<form  action="/appsflyer/upload" name="report" enctype="multipart/form-data" method="GET">
<fieldset>
    <legend><h3>Get CSV from AppsFlyer</h3></legend>
From: date <input type="date" name="from_date" id="from_date" value="YYYY-MM-DD"> time <input type="time" name="from_time" value="HH:MM"><br/><br/>
To: date <input type="date" name="to_date" value="YYYY-MM-DD"> time <input type="time" name="to_time" value="HH:MM"><br/><br/>
<button name="get_report">  Get CSV </button> <br/> <br/>
</fieldset>
</form><p/>
-->
<div>
    <a href="/commercials">
        Costs and Revenues recalculation configuration (Commercials).
    </a>
</div><br/>
<form action="/appsflyer/upload" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
    <legend><h3>Enter Injected Parameters</h3></legend>
    Advertiser: <input type="text" name="advertiser" id="advertiser" ><br/><br/>
    Offer Name: <input type="text" name="offer_name" id="offer_name"><br/><br/>
    Offer ID: <input type="text" name="offer_id" id="offer_id" required><br/><br/>
    Offer URL: <input type="text" name="offer_url" id="offer_url" ><br/><br/>
    Offer Preview URL: <input type="text" name="offer_preview_url" id="offer_preview_url" ><br/><br/>
    OS: <input type="text" name="os" id="os" ><br/><br/>
    Platform Name: <input type="text" name="platform_name" id="platform_name" ><br/><br/>
    Business model: <input type="text" name="business_model" id="business_model" ><br/><br/>
    Commission: <input type="number" step="any" min="0" name="commission" id="commission" ><br/><br/>
    Platform Type: <select name="platform_type" >
        <option value="tracking"> Tracking </option>
    </select><br><br/>
    Report Type: <select name="report_type">
        <option value="install_report">Install report</option>
        <option value="in_app_event_report">In App Event report</option>
    </select><br/><br/>
    <input type="file" name="file"><br/><br/>
    <input type="submit" name="import" value="Import Now"> &nbsp&nbsp&nbsp
</fieldset>

</form>

<form action="/appsflyer/uploadFromWeb" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Upload from Appsflyer</h3></legend>
        Web Date Start : <input type="date" name="from_date" id="from_date">&nbsp&nbsp&nbsp
        Web Date End : <input type="date" name="to_date" id="to_date"><br/><br/>
        <input type="radio" name="radios" value="both" checked>Both types &nbsp&nbsp&nbsp
        <input type="radio" name="radios" value="install_report">Only Installs &nbsp&nbsp&nbsp
        <input type="radio" name="radios" value="in_app_event_report"> Only In App Events <br/><br/>
        <input type="radio" name="table" value="general" checked>To appsflyer_events table &nbsp&nbsp&nbsp
        <input type="radio" name="table" value="full">To appsflyer_events_full table <br/><br/>
        Offer ID: <input type="text" name="offer_id" id="offer_id"><br/><br/>
        <input type="submit" name="import" value="Import Now">
    </fieldset>

</form>

<p/>
<form  action="/appsflyer/generateReport" name="request" enctype="multipart/form-data" method="GET">
 <fieldset>
  <legend><h3>Get Report</h3></legend>

      Report Name: <select name="report_name">
<option name="simple_uaba" value="simple_uaba">Simple User Activity By Age</option>
<option name="uaba" value="uaba">User Activity By Age</option>
<option name="advanced-uaba" value="advanced-uaba">Advanced User Activity By Age</option>

</select><br/><br/>



     Install Date Start : <input type="date" name="from_date" id="from_date"> &nbsp&nbsp&nbsp Install Date End : <input type="date" name="to_date" id="to_date"><br><br/>
     Offer Name: <input type="text" name="offer_name">&nbsp&nbsp&nbsp&nbsp&nbsp
     Country: <input type="text" name="country"><br/><br/>
      Agency: <input type="text" name="agency">&nbsp&nbsp&nbsp&nbsp&nbsp
      Media Source: <input type="text" name="media_source"><br/><br/>
      Campaign: <input type="text" name="campaign">&nbsp&nbsp&nbsp&nbsp&nbsp
      Site ID: <input type="text" name="site_id"><br/><br/>

      <input type="submit" name="report" value="Get Report">&nbsp&nbsp&nbsp&nbsp&nbsp


      <!--<input type="button" name="export_to_csv" value="Export to CSV"> -->
      <br/><br/>


  </fieldset></form>

  <form  action="/appsflyer/generateOptimizedReport" name="request" enctype="multipart/form-data" method="GET">
   <fieldset>
    <legend><h3>Get Optimized Report</h3></legend>
       Install Date Start : <input type="date" name="from_date" id="from_date"> &nbsp&nbsp&nbsp Install Date End : <input type="date" name="to_date" id="to_date"><br><br/>
        <input type="submit" name="report" value="Get Report">&nbsp&nbsp&nbsp&nbsp&nbsp
        <br/><br/>
    </fieldset>
  </form>

    <form  action="/appsflyer/exportToCooladataWeb" name="submit" enctype="multipart/form-data" method="POST">
        <fieldset>
        <legend><h3>Export to Cooladata</h3></legend>
        Where: <textarea name="where" cols=40 rows=4></textarea><br/><br/>
        <input type="submit" name="cooladata" value="Export">
        <br/><br/>
        </fieldset>
    </form>

    <form  action="/appsflyer/uploadFromWebAndExportToCooladata" name="submit" enctype="multipart/form-data" method="POST">
        <fieldset>
            <legend><h3>Upload From Web And Export To Cooladata</h3></legend>
            <input type="submit" name="cooladata" value="Export">
            <br/><br/>
        </fieldset>
    </form>

    <form  action="/appsflyer/ImportAppLovinCostsFromFile" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Import AppLovin Costs from CSV</h3></legend>
        <input type="file" name="costsfile"><br/><br/>
        <input type="submit" name="costimport" value="Import Now">
        <br/><br/>
    </fieldset></form>

<form  action="/appsflyer/ImportCostsFromMediaSources" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Import All New Costs From Media Sources</h3></legend>
        <input type="submit" name="importCostsFromMediaSources" value="Import Now">
        <br/><br/>
    </fieldset>
</form>

    <form  action="/appsflyer/ImportCostsFromAppLovin" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Import All New Costs From AppLovin</h3></legend>
        <input type="submit" name="applovincostimport" value="Import Now">
        <br/><br/>
    </fieldset>
    </form>

    <form  action="/appsflyer/ImportFromAdColony" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Import All New Costs From AdColony</h3></legend>
        <input type="submit" name="importFromAdColony" value="Import Now">
        <br/><br/>
    </fieldset>
    </form>

    <form  action="/appsflyer/ImportFromHeyzap" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Import All New Costs From Heyzap</h3></legend>
        <input type="submit" name="importFromHeyzap" value="Import Now">
        <br/><br/>
    </fieldset>
    </form>

    <form  action="/appsflyer/ImportCostsFromVungle" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Import All New Costs From Vungle</h3></legend>
        <input type="submit" name="importCostsFromVungle" value="Import Now">
        <br/><br/>
    </fieldset>
    </form>
<form  action="/appsflyer/importCostsFromSuperSonic" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Import All New Costs From SuperSonic</h3></legend>
        <input type="submit" name="importCostsFromSuperSonic" value="Import Now">
        <br/><br/>
    </fieldset>
</form>
<form  action="/appsflyer/importCostsFromUnityAds" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Import All New Costs From Unity Ads</h3></legend>
        <input type="submit" name="importCostsFromUnityAds" value="Import Now">
        <br/><br/>
    </fieldset>
</form>
<form  action="/appsflyer/importCostsFromVoluum" name="submit" enctype="multipart/form-data" method="POST">
    <fieldset>
        <legend><h3>Import All New Costs From Voluum</h3></legend>
        <input type="submit" name="importCostsFromVoluum" value="Import Now">
        <br/><br/>
    </fieldset>
</form>

</body>
</html>