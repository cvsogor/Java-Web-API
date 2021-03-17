DROP PROCEDURE IF EXISTS generate_month_report;
DELIMITER //

CREATE PROCEDURE generate_month_report(start_date date, end_date date)
BEGIN
select 
	cast(A.install_time as CHAR(2)) 'install_month' , 
    A.offer_name,
    ifnull(A.Revenue, 0) 'Revenue',
    ifnull(A.Cost, 0) 'Cost',
    0 'impressions', 
    0 'Clicks', 
    0 'CTR', 
    ifnull(B.Installs, 0) 'Installs', 
    ifnull(A.Cost/B.Installs, 0) 'eCPI', 
    ifnull(C.FD_count, 0) 'FD_count',
    ifnull(C.FD_amount, 0) 'FD_amount',
    ifnull(D.deposits_count, 0) 'deposits_count',
    ifnull(D.deposits_amount, 0) 'deposits_amount',
    ifnull(B.Installs/D.deposits_amount, 0) 'installs/deposit_amount',
    ifnull(A.Cost/C.FD_count, 0) 'eCPA',
    ifnull(B.Installs/C.FD_count, 0) 'Installs_to_RFD',
    ifnull(B.Installs/D.deposits_amount, 0) 'ARPU',
    ifnull(C.FD_count/D.deposits_amount, 0) 'ARPPU',
    0 'CPC',
    ifnull(A.Cost/D.deposits_amount, 0) 'ROI',
    0 'CTI',
    0 'CTD',
    0 'eCPM'
    
from
(
	select 
		extract(month from install_time) 'install_time', 
        offer_name, 
        sum(cost) 'Cost',
        sum(real_revenue) 'Revenue'
	from appsflyer_events
	where date(install_time) between start_date and end_date
    group by extract(month from install_time), offer_name    
) as A
left join 
(
	select 
		extract(month from install_time) 'install_time', 
        offer_name, 
        count(1) 'Installs'
	from appsflyer_events
	where event_name='install' and date(install_time) between start_date and end_date
	group by extract(month from event_time), offer_name
) as B
on A.install_time = B.install_time and A.offer_name = B.offer_name
left join
(
	select 
		extract(month from install_time) 'install_time', 
        offer_name, 
        count(1) 'FD_count',
        sum(event_revenue) 'FD_amount' 
	from appsflyer_events
	where event_name='ftd' and date(install_time) between start_date and end_date
	group by extract(month from install_time), offer_name
) as C
on A.install_time = C.install_time and A.offer_name = C.offer_name
left join
(
	select 
		extract(month from install_time) 'install_time', 
        offer_name, 
        count(1) 'deposits_count', 
        sum(event_revenue) 'deposits_amount'
	from appsflyer_events
	where event_name='deposit' and date(install_time) between start_date and end_date
	group by extract(month from event_time), offer_name
) as D
on A.install_time = D.install_time and A.offer_name = D.offer_name;
END
