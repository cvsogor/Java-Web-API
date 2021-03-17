drop table if exists appsflyer_tmp_inapp_events;
drop table if exists appsflyer_tmp_install_events;
drop table if exists appsflyer_events;
drop table if exists appsflyer_events_full;
drop table if exists appsflyer_import_config;
drop table if exists appsflyer_import_config_full;
drop table if exists settings;
drop table if exists account_management;
drop table if exists cooladata_export_filter;

CREATE TABLE appsflyer_tmp_inapp_events (impression_time DATETIME NULL DEFAULT NULL, click_time DATETIME, install_time DATETIME
, event_time DATETIME NULL DEFAULT NULL, event_name VARCHAR(255), event_value VARCHAR(255), event_revenue DOUBLE, currency VARCHAR(255), receipt_id VARCHAR(255)
, is_validated VARCHAR(255), agency_pmd VARCHAR(255), media_source VARCHAR(255), channel VARCHAR(255), keywords VARCHAR(255)
, campaign_name VARCHAR(255), campaign_id VARCHAR(255), adset_name VARCHAR(255), adset_id VARCHAR(255), ad_name VARCHAR(255)
, ad_id VARCHAR(255), ad_type VARCHAR(255), site_id VARCHAR(255), cost_model VARCHAR(255), cost DOUBLE, cost_currency VARCHAR(255)
, region VARCHAR(255), country_code VARCHAR(6), state VARCHAR(255), city VARCHAR(255), ip VARCHAR(255), wifi VARCHAR(255), language VARCHAR(255)
, event_source VARCHAR(255), appsflyer_device_id VARCHAR(255), customer_user_id VARCHAR(255), idfa VARCHAR(255), idfv VARCHAR(255), mac VARCHAR(255)
, device_name VARCHAR(255), devise_type VARCHAR(255), os_version VARCHAR(255), sdk_version VARCHAR(255), app_version VARCHAR(255), sub_param_1 VARCHAR(255)
, sub_param_2 VARCHAR(255), sub_param_3 VARCHAR(255), sub_param_4 VARCHAR(255), sub_param_5 VARCHAR(255), impression_url VARCHAR(2000), click_url VARCHAR(2000)
, http_referrer VARCHAR(255), contributor_1_media_source VARCHAR(255), contributor_1_campaign VARCHAR(255), contributor_2_media_source VARCHAR(255)
, contributor_2_campaign VARCHAR(255), contributor_3_media_source VARCHAR(255), contributor_3_campaign VARCHAR(255)
, android_id VARCHAR(255), imei VARCHAR(255), advertising_id VARCHAR(255), operator VARCHAR(255), carrier VARCHAR(255)
, installer_package VARCHAR(255), normalized_event_name VARCHAR(255)
);

CREATE TABLE appsflyer_tmp_install_events (impression_time DATETIME NULL DEFAULT NULL, click_time DATETIME, install_time DATETIME, agency_pmd VARCHAR(255), media_source VARCHAR(255), channel VARCHAR(255)
, keywords VARCHAR(255), campaign_name VARCHAR(255), campaign_id VARCHAR(255), adset_name VARCHAR(255), adset_id VARCHAR(255), ad_name VARCHAR(255), ad_id VARCHAR(255)
, ad_type VARCHAR(255), site_id VARCHAR(255), cost_model VARCHAR(255), cost DOUBLE, cost_currency VARCHAR(255), region VARCHAR(255), country_code VARCHAR(6)
, state VARCHAR(255), city VARCHAR(255), ip VARCHAR(255), wifi VARCHAR(255), language VARCHAR(255), event_source VARCHAR(255), appsflyer_device_id VARCHAR(255)
, customer_user_id VARCHAR(255), idfa VARCHAR(255), idfv VARCHAR(255), mac VARCHAR(255), device_name VARCHAR(255), devise_type VARCHAR(255), os_version VARCHAR(255)
, sdk_version VARCHAR(255), app_version VARCHAR(255), sub_param_1 VARCHAR(255), sub_param_2 VARCHAR(255), sub_param_3 VARCHAR(255), sub_param_4 VARCHAR(255)
, sub_param_5 VARCHAR(255), impression_url VARCHAR(2000), click_url VARCHAR(2000), http_referrer VARCHAR(255), contributor_1_media_source VARCHAR(255)
, contributor_1_campaign VARCHAR(255), contributor_2_media_source VARCHAR(255), contributor_2_campaign VARCHAR(255), contributor_3_media_source VARCHAR(255)
, contributor_3_campaign VARCHAR(255), android_id VARCHAR(255), imei VARCHAR(255), advertising_id VARCHAR(255), operator VARCHAR(255), carrier VARCHAR(255)
, installer_package VARCHAR(255)
);

CREATE TABLE appsflyer_events (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, impression_time DATETIME NULL DEFAULT NULL, click_time DATETIME
, install_time DATETIME NULL DEFAULT NULL, event_time DATETIME, event_name VARCHAR(255), original_event_name VARCHAR(255), event_value VARCHAR(255)
, event_revenue DOUBLE, currency VARCHAR(255), receipt_id VARCHAR(255), is_validated VARCHAR(10), agency_pmd VARCHAR(255), media_source VARCHAR(255)
,channel VARCHAR(255), keywords VARCHAR(255), campaign_name VARCHAR(255), site_id VARCHAR(255), campaign_id VARCHAR(255), adset_name VARCHAR(255), adset_id VARCHAR(255)
, ad_name VARCHAR(255), ad_id VARCHAR(255), ad_type VARCHAR(255), cost_model VARCHAR(255), cost DOUBLE, cost_currency VARCHAR(255), region VARCHAR(255)
, country_code VARCHAR(255), state VARCHAR(255), city VARCHAR(255), ip VARCHAR(15), wifi VARCHAR(10),language VARCHAR(255), event_source VARCHAR(255)
, appsflyer_device_id VARCHAR(255), customer_user_id VARCHAR(255), idfa VARCHAR(255), idfv VARCHAR(255), android_id VARCHAR(255), imei VARCHAR(255), mac VARCHAR(255)
, device_name VARCHAR(255), advertising_id VARCHAR(255), devise_type VARCHAR(255), os_version VARCHAR(255), sdk_version VARCHAR(15), app_version VARCHAR(255)
,operator VARCHAR(255), carrier VARCHAR(255), sub_param_1 VARCHAR(255), sub_param_2 VARCHAR(255), sub_param_3 VARCHAR(255), sub_param_4 VARCHAR(255)
, sub_param_5 VARCHAR(255), impression_url VARCHAR(2000), click_url VARCHAR(2000), http_referrer VARCHAR(255), installer_package VARCHAR(255)
, contributor_1_media_source VARCHAR(255), contributor_1_campaign VARCHAR(255), contributor_2_media_source VARCHAR(255), contributor_2_campaign VARCHAR(255)
, contributor_3_media_source VARCHAR(255), contributor_3_campaign VARCHAR(255), os VARCHAR(255), creative VARCHAR(255), targeting_type VARCHAR(255)
, targeting_group VARCHAR(255), gender VARCHAR(10), bid VARCHAR(255), real_cost DOUBLE, real_revenue DOUBLE, reported_platform VARCHAR(255), platform_type VARCHAR(255)
, advertiser VARCHAR(255), offer_id VARCHAR(255), offer_name VARCHAR(255), offer_url VARCHAR(2000), offer_preview_url VARCHAR(2000), calc_cost DOUBLE, business_model VARCHAR(255), commission double
, import_time DATETIME, export_time DATETIME
, UNIQUE INDEX (event_time, event_name, appsflyer_device_id, offer_id, media_source, agency_pmd, campaign_id));

CREATE TABLE appsflyer_events_full (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, impression_time DATETIME NULL DEFAULT NULL, click_time DATETIME
  , install_time DATETIME NULL DEFAULT NULL, event_time DATETIME, event_name VARCHAR(255), original_event_name VARCHAR(255), event_value VARCHAR(255)
  , event_revenue DOUBLE, currency VARCHAR(255), receipt_id VARCHAR(255), is_validated VARCHAR(10), agency_pmd VARCHAR(255), media_source VARCHAR(255)
  ,channel VARCHAR(255), keywords VARCHAR(255), campaign_name VARCHAR(255), site_id VARCHAR(255), campaign_id VARCHAR(255), adset_name VARCHAR(255), adset_id VARCHAR(255)
  , ad_name VARCHAR(255), ad_id VARCHAR(255), ad_type VARCHAR(255), cost_model VARCHAR(255), cost DOUBLE, cost_currency VARCHAR(255), region VARCHAR(255)
  , country_code VARCHAR(255), state VARCHAR(255), city VARCHAR(255), ip VARCHAR(15), wifi VARCHAR(10),language VARCHAR(255), event_source VARCHAR(255)
  , appsflyer_device_id VARCHAR(255), customer_user_id VARCHAR(255), idfa VARCHAR(255), idfv VARCHAR(255), android_id VARCHAR(255), imei VARCHAR(255), mac VARCHAR(255)
  , device_name VARCHAR(255), advertising_id VARCHAR(255), devise_type VARCHAR(255), os_version VARCHAR(255), sdk_version VARCHAR(15), app_version VARCHAR(255)
  ,operator VARCHAR(255), carrier VARCHAR(255), sub_param_1 VARCHAR(255), sub_param_2 VARCHAR(255), sub_param_3 VARCHAR(255), sub_param_4 VARCHAR(255)
  , sub_param_5 VARCHAR(255), impression_url VARCHAR(2000), click_url VARCHAR(2000), http_referrer VARCHAR(255), installer_package VARCHAR(255)
  , contributor_1_media_source VARCHAR(255), contributor_1_campaign VARCHAR(255), contributor_2_media_source VARCHAR(255), contributor_2_campaign VARCHAR(255)
  , contributor_3_media_source VARCHAR(255), contributor_3_campaign VARCHAR(255), os VARCHAR(255), creative VARCHAR(255), targeting_type VARCHAR(255)
  , targeting_group VARCHAR(255), gender VARCHAR(10), bid VARCHAR(255), real_cost DOUBLE, real_revenue DOUBLE, reported_platform VARCHAR(255), platform_type VARCHAR(255)
  , advertiser VARCHAR(255), offer_id VARCHAR(255), offer_name VARCHAR(255), offer_url VARCHAR(2000), offer_preview_url VARCHAR(2000), calc_cost DOUBLE, business_model VARCHAR(255), commission double
  , UNIQUE INDEX (event_time, event_name, appsflyer_device_id, offer_id, media_source, agency_pmd, campaign_id));

CREATE TABLE appsflyer_import_config (business_model VARCHAR(255), commission DOUBLE, advertiser VARCHAR(255), offer_name VARCHAR(255), offer_id VARCHAR(255), os VARCHAR(255)
  , offer_url VARCHAR(2000), offer_preview_url VARCHAR(2000), token VARCHAR(255), reported_platform VARCHAR(255), platform_type VARCHAR(255)
);

CREATE TABLE appsflyer_import_config_full (business_model VARCHAR(255), commission DOUBLE, advertiser VARCHAR(255), offer_name VARCHAR(255), offer_id VARCHAR(255), os VARCHAR(255)
  , offer_url VARCHAR(2000), offer_preview_url VARCHAR(2000), token VARCHAR(255), reported_platform VARCHAR(255), platform_type VARCHAR(255)
);

CREATE TABLE applovin_costs_report_new (day_date DATETIME, campaign VARCHAR(255), country VARCHAR(2), impressions int, clicks int, ctr double,
CONSTRAINT pk_costs_report PRIMARY KEY (day_date , campaign,country,impressions,clicks,ctr));

INSERT INTO appsflyer_import_config VALUES ('Network',NULL,'InfiApps','Slot Bonanza - iOS','id556627912','iOS','itunes.apple.com/gb/app/id556627912','itunes.apple.com/gb/app/id556627912','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'InfiApps','Slot Bonanza HD - iOS','id557155750','iOS','itunes.apple.com/gb/app/id557155750','itunes.apple.com/gb/app/id557155750','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'InfiApps','Slot Bonanza - Android','com.infiapps.slotbonanza','Android','play.google.com/store/apps/details?id=com.infiapps.slotbonanza','play.google.com/store/apps/details?id=com.infiapps.slotbonanza','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',12,'Playtika','BINGO Blitz - iOS','id529996768','iOS','itunes.apple.com/gb/app/id529996768','itunes.apple.com/gb/app/id529996768','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',12,'Playtika','BINGO Blitz - Android','air.com.buffalo_studios.newflashbingo','Android','play.google.com/store/apps/details?id=air.com.buffalo_studios.newflashbingo','play.google.com/store/apps/details?id=air.com.buffalo_studios.newflashbingo','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'Playtika','Caesars Slots - iOS','id603097018','iOS','itunes.apple.com/gb/app/id603097018','itunes.apple.com/gb/app/id603097018','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'Playtika','Caesars Slots HD - iOS','id718985796','iOS','itunes.apple.com/gb/app/id718985796','itunes.apple.com/gb/app/id718985796','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'Playtika','Caesars Slots  - Android','com.playtika.caesarscasino','Android','play.google.com/store/apps/details?id=com.playtika.caesarscasino','play.google.com/store/apps/details?id=com.playtika.caesarscasino','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',12,'Playtika','Poker Friends Texas Holdem - Android','com.bigblueparrot.pokerfriends','Android','play.google.com/store/apps/details?id=com.bigblueparrot.pokerfriends','play.google.com/store/apps/details?id=com.bigblueparrot.pokerfriends','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',12,'Playtika','Poker Friends Texas Holdem - iOS','id480523695','iOS','itunes.apple.com/gb/app/id480523695','itunes.apple.com/gb/app/id480523695','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',12,'Playtika','Poker Friends HD - iOS','id778713740','iOS','itunes.apple.com/gb/app/id778713740','itunes.apple.com/gb/app/id778713740','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',12,'HOF','House of Fun - Android','com.pacificinteractive.HouseOfFun','Android','play.google.com/store/apps/details?id=com.pacificinteractive.HouseOfFun','play.google.com/store/apps/details?id=com.pacificinteractive.HouseOfFun','caf42e2b-64ea-4ec9-b7e9-0fd83ebae8cf','appsflyer','tracking')
  ,('Agency',12,'HOF','House of Fun - iOS','id586634331','iOS','itunes.apple.com/gb/app/id586634331','itunes.apple.com/gb/app/id586634331','caf42e2b-64ea-4ec9-b7e9-0fd83ebae8cf','appsflyer','tracking')
  ,('Network',NULL,'BWIN','BWIN sports - iOS','id393760245','iOS','itunes.apple.com/gb/app/id393760245','itunes.apple.com/gb/app/id393760245','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'xtreme slots','Xtreme Slots - Android','net.odasoft.xtremeslots','Android','play.google.com/store/apps/details?id=net.odasoft.xtremeslots','play.google.com/store/apps/details?id=net.odasoft.xtremeslots','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'xtreme slots','Xtreme Slots  - iOS','id553573601','iOS','itunes.apple.com/gb/app/id553573601','itunes.apple.com/gb/app/id553573601','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'xWise','Titanbet Sports Betting - iOS','id936312591','iOS','itunes.apple.com/gb/app/id936312591','itunes.apple.com/gb/app/id936312591','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'xWise','Titan bet UK - Android','uk.co.titanbet.android-standalone','Android','play.google.com/store/apps/details?id=uk.co.titanbet.android-standalone','play.google.com/store/apps/details?id=uk.co.titanbet.android-standalone','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'xWise','Europa casino - Android','com.europa.livecasino-standalone','Android','play.google.com/store/apps/details?id=com.europa.livecasino-standalone','play.google.com/store/apps/details?id=com.europa.livecasino-standalone','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'xWise','Europa casino - iOS','id852489515','iOS','itunes.apple.com/gb/app/id852489515','itunes.apple.com/gb/app/id852489515','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'old vegas slots','Old Vegas Slots - Android','com.DgnGames.OldVegasSlots','Android','play.google.com/store/apps/details?id=com.DgnGames.OldVegasSlots','play.google.com/store/apps/details?id=com.DgnGames.OldVegasSlots','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'old vegas slots','Old Vegas Slots - iOS','id919141988','iOS','itunes.apple.com/gb/app/id919141988','itunes.apple.com/gb/app/id919141988','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'BetFred','BetFred - iOS','id632113432','iOS','itunes.apple.com/gb/app/id632113432','itunes.apple.com/gb/app/id632113432','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'ProductMadness','Heart of Vegas - Android','com.productmadness.hovmobile','Android','play.google.com/store/apps/details?id=com.productmadness.hovmobile','play.google.com/store/apps/details?id=com.productmadness.hovmobile','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'ProductMadness','Heart Of Vegas - iOS','id785537179','iOS','itunes.apple.com/gb/app/id785537179','itunes.apple.com/gb/app/id785537179','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',10,'RTG','Black Diamond Casino - Android','com.risingtidegames.blackdiamondslots.beta','Android','play.google.com/store/apps/details?id=com.risingtidegames.blackdiamondslots.beta','play.google.com/store/apps/details?id=com.risingtidegames.blackdiamondslots.beta','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',10,'RTG','Black Diamond Casino - iOS','id962510268','iOS','itunes.apple.com/gb/app/id962510268','itunes.apple.com/gb/app/id962510268','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',10,'RTG','Vegas Diamond Slots - Android','com.zynga.vegasdiamondslots','Android','play.google.com/store/apps/details?id=com.zynga.vegasdiamondslots','play.google.com/store/apps/details?id=com.zynga.vegasdiamondslots','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','BetStars sports betting - EU - iOS','id1012933302','iOS','itunes.apple.com/gb/app/id1012933302','itunes.apple.com/gb/app/id1012933302','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','BetStars sports betting - UK - iOS','id1012933972','iOS','itunes.apple.com/gb/app/id1012933972','itunes.apple.com/gb/app/id1012933972','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','BetStars sports betting - ES - iOS','id533787554','iOS','itunes.apple.com/gb/app/id533787554','itunes.apple.com/gb/app/id533787554','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','Pokerstars poker - UK - iOS','id897137765','iOS','itunes.apple.com/gb/app/id897137765','itunes.apple.com/gb/app/id897137765','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','Pokerstars poker - FR - iOS','id509469724','iOS','itunes.apple.com/gb/app/id509469724','itunes.apple.com/gb/app/id509469724','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','Pokerstars Poker Texas Holdem - iOS','id497361777','iOS','itunes.apple.com/gb/app/id497361777','itunes.apple.com/gb/app/id497361777','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Agency',NULL,'The Lotter','The Lotter - Android','com.LotteryResults.gini_apps','Android','play.google.com/store/apps/details?id=com.LotteryResults.gini_apps','play.google.com/store/apps/details?id=com.LotteryResults.gini_apps','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'AGS','Vegas Fever - Android','casino.slots.vegasfever','Android','play.google.com/store/apps/details?id=casino.slots.vegasfever','play.google.com/store/apps/details?id=casino.slots.vegasfever','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'AGS','Lucky Play Casino - iOS','id624512118','iOS','itunes.apple.com/gb/app/id624512118','itunes.apple.com/gb/app/id624512118','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking')
  ,('Network',NULL,'888','888sport - Online sports betting - iOS','id530469592','iOS','itunes.apple.com/gb/app/id530469592','itunes.apple.com/gb/app/id530469592','74e804d4-0a1c-4cab-8c7f-389663428df7','appsflyer','tracking');

INSERT INTO appsflyer_import_config_full VALUES ('Network',NULL,'InfiApps','Slot Bonanza - iOS','id556627912','iOS','itunes.apple.com/gb/app/id556627912','itunes.apple.com/gb/app/id556627912','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'InfiApps','Slot Bonanza HD - iOS','id557155750','iOS','itunes.apple.com/gb/app/id557155750','itunes.apple.com/gb/app/id557155750','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'InfiApps','Slot Bonanza - Android','com.infiapps.slotbonanza','Android','play.google.com/store/apps/details?id=com.infiapps.slotbonanza','play.google.com/store/apps/details?id=com.infiapps.slotbonanza','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',12,'Winner','Winner Sports Betting','id650795861','iOS','itunes.apple.com/gb/app/id650795861','itunes.apple.com/gb/app/id650795861','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',12,'Playtika','BINGO Blitz - iOS','id529996768','iOS','itunes.apple.com/gb/app/id529996768','itunes.apple.com/gb/app/id529996768','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',12,'Playtika','BINGO Blitz - Android','air.com.buffalo_studios.newflashbingo','Android','play.google.com/store/apps/details?id=air.com.buffalo_studios.newflashbingo','play.google.com/store/apps/details?id=air.com.buffalo_studios.newflashbingo','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'Playtika','Caesars Slots - iOS','id603097018','iOS','itunes.apple.com/gb/app/id603097018','itunes.apple.com/gb/app/id603097018','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'Playtika','Caesars Slots HD - iOS','id718985796','iOS','itunes.apple.com/gb/app/id718985796','itunes.apple.com/gb/app/id718985796','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'Playtika','Caesars Slots  - Android','com.playtika.caesarscasino','Android','play.google.com/store/apps/details?id=com.playtika.caesarscasino','play.google.com/store/apps/details?id=com.playtika.caesarscasino','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',12,'Playtika','Poker Friends Texas Holdem - Android','com.bigblueparrot.pokerfriends','Android','play.google.com/store/apps/details?id=com.bigblueparrot.pokerfriends','play.google.com/store/apps/details?id=com.bigblueparrot.pokerfriends','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',12,'Playtika','Poker Friends Texas Holdem - iOS','id480523695','iOS','itunes.apple.com/gb/app/id480523695','itunes.apple.com/gb/app/id480523695','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',12,'Playtika','Poker Friends HD - iOS','id778713740','iOS','itunes.apple.com/gb/app/id778713740','itunes.apple.com/gb/app/id778713740','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',12,'HOF','House of Fun - Android','com.pacificinteractive.HouseOfFun','Android','play.google.com/store/apps/details?id=com.pacificinteractive.HouseOfFun','play.google.com/store/apps/details?id=com.pacificinteractive.HouseOfFun','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',12,'HOF','House of Fun - iOS','id586634331','iOS','itunes.apple.com/gb/app/id586634331','itunes.apple.com/gb/app/id586634331','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'BWIN','BWIN sports - iOS','id393760245','iOS','itunes.apple.com/gb/app/id393760245','itunes.apple.com/gb/app/id393760245','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'xtreme slots','Xtreme Slots - Android','net.odasoft.xtremeslots','Android','play.google.com/store/apps/details?id=net.odasoft.xtremeslots','play.google.com/store/apps/details?id=net.odasoft.xtremeslots','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'xtreme slots','Xtreme Slots  - iOS','id553573601','iOS','itunes.apple.com/gb/app/id553573601','itunes.apple.com/gb/app/id553573601','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'xWise','Titanbet Sports Betting - iOS','id936312591','iOS','itunes.apple.com/gb/app/id936312591','itunes.apple.com/gb/app/id936312591','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'xWise','Titan bet UK - Android','uk.co.titanbet.android-standalone','Android','play.google.com/store/apps/details?id=uk.co.titanbet.android-standalone','play.google.com/store/apps/details?id=uk.co.titanbet.android-standalone','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'xWise','Europa casino - Android','com.europa.livecasino-standalone','Android','play.google.com/store/apps/details?id=com.europa.livecasino-standalone','play.google.com/store/apps/details?id=com.europa.livecasino-standalone','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'xWise','Europa casino - iOS','id852489515','iOS','itunes.apple.com/gb/app/id852489515','itunes.apple.com/gb/app/id852489515','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'old vegas slots','Old Vegas Slots - Android','com.DgnGames.OldVegasSlots','Android','play.google.com/store/apps/details?id=com.DgnGames.OldVegasSlots','play.google.com/store/apps/details?id=com.DgnGames.OldVegasSlots','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'old vegas slots','Old Vegas Slots - iOS','id919141988','iOS','itunes.apple.com/gb/app/id919141988','itunes.apple.com/gb/app/id919141988','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'BetFred','BetFred - iOS','id632113432','iOS','itunes.apple.com/gb/app/id632113432','itunes.apple.com/gb/app/id632113432','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'ProductMadness','Heart of Vegas - Android','com.productmadness.hovmobile','Android','play.google.com/store/apps/details?id=com.productmadness.hovmobile','play.google.com/store/apps/details?id=com.productmadness.hovmobile','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'ProductMadness','Heart Of Vegas - iOS','id785537179','iOS','itunes.apple.com/gb/app/id785537179','itunes.apple.com/gb/app/id785537179','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',10,'RTG','Black Diamond Casino - Android','com.risingtidegames.blackdiamondslots.beta','Android','play.google.com/store/apps/details?id=com.risingtidegames.blackdiamondslots.beta','play.google.com/store/apps/details?id=com.risingtidegames.blackdiamondslots.beta','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',10,'RTG','Black Diamond Casino - iOS','id962510268','iOS','itunes.apple.com/gb/app/id962510268','itunes.apple.com/gb/app/id962510268','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',10,'RTG','Vegas Diamond Slots - Android','com.zynga.vegasdiamondslots','Android','play.google.com/store/apps/details?id=com.zynga.vegasdiamondslots','play.google.com/store/apps/details?id=com.zynga.vegasdiamondslots','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','BetStars sports betting - EU - iOS','id1012933302','iOS','itunes.apple.com/gb/app/id1012933302','itunes.apple.com/gb/app/id1012933302','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','BetStars sports betting - UK - iOS','id1012933972','iOS','itunes.apple.com/gb/app/id1012933972','itunes.apple.com/gb/app/id1012933972','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','BetStars sports betting - ES - iOS','id533787554','iOS','itunes.apple.com/gb/app/id533787554','itunes.apple.com/gb/app/id533787554','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','Pokerstars poker - UK - iOS','id897137765','iOS','itunes.apple.com/gb/app/id897137765','itunes.apple.com/gb/app/id897137765','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','Pokerstars poker - FR - iOS','id509469724','iOS','itunes.apple.com/gb/app/id509469724','itunes.apple.com/gb/app/id509469724','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'PokerStars','Pokerstars Poker Texas Holdem - iOS','id497361777','iOS','itunes.apple.com/gb/app/id497361777','itunes.apple.com/gb/app/id497361777','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',NULL,'The Lotter','The Lotter - Android','com.LotteryResults.gini_apps','Android','play.google.com/store/apps/details?id=com.LotteryResults.gini_apps','play.google.com/store/apps/details?id=com.LotteryResults.gini_apps','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'AGS','Vegas Fever - Android','casino.slots.vegasfever','Android','play.google.com/store/apps/details?id=casino.slots.vegasfever','play.google.com/store/apps/details?id=casino.slots.vegasfever','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'AGS','Lucky Play Casino - iOS','id624512118','iOS','itunes.apple.com/gb/app/id624512118','itunes.apple.com/gb/app/id624512118','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',NULL,'Wizits','Slotagram - Android','air.com.wizits.slotagram.android','Android','play.google.com/store/apps/details?id=air.com.wizits.slotagram.android','play.google.com/store/apps/details?id=air.com.wizits.slotagram.android','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',NULL,'Mytopia','BackGammon Live - Android','air.com.come2play.backgammon','Android','play.google.com/store/apps/details?id=air.com.come2play.backgammon','play.google.com/store/apps/details?id=air.com.come2play.backgammon','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Agency',NULL,'Mytopia','BackGammon Live - iOS','id739095641','iOS','itunes.apple.com/gb/app/id739095641','itunes.apple.com/gb/app/id739095641','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking')
  ,('Network',NULL,'888','888sport - Online sports betting - iOS','id530469592','iOS','itunes.apple.com/gb/app/id530469592','itunes.apple.com/gb/app/id530469592','8b467209-4c41-47ff-a7f4-4cd588947b70','appsflyer','tracking');

CREATE TABLE settings (setting_key VARCHAR(255), value VARCHAR(10000)
  ,PRIMARY KEY (setting_key));

REPLACE INTO settings
VALUES ("event_normalization", "{\"evetentForNormalization\":[
{
\"normalizedEventName\":\"Login\",
\"replaceableNames\":[
\"af_login\",
\"login\"
]
},
{
\"normalizedEventName\":\"Deposit\",
\"replaceableNames\":[
\"af_purchase\",
\"In-App-Purchase\",
\"revenue\",
\"Purchase\",
\"deposit\",
\"player_payment\",
\"subsequent deposit\",
\"payment\"
]
},
{
\"normalizedEventName\":\"FTD\",
\"replaceableNames\":[
\"af_ftd\",
\"ftd\",
\"player_ftd\",
\"first deposit\"
]
},
{
\"normalizedEventName\":\"FB Login\",
\"replaceableNames\":[
\"FBlogin\",
\"fb_connect\"
]
}
]
}");

REPLACE INTO settings
VALUES ("last_imported_to_cooladata", "0");

CREATE TABLE account_management (department VARCHAR(255) NOT NULL, am_name VARCHAR(255) NOT NULL, cooladata_username VARCHAR(255) NOT NULL
  , business_model VARCHAR(255) NOT NULL, offer_id VARCHAR(255), reported_platform VARCHAR(255) NOT NULL, media_source VARCHAR(255), permission_type VARCHAR(255) NOT NULL
  , UNIQUE(offer_id, reported_platform, media_source));

INSERT INTO `account_management` VALUES ('Agency-Networks','Ariel Cohen','ArielC','Agency','House of fun - Android','appsflyer','Glispa_int','Read\r')
  ,('Agency-Networks','Ariel Cohen','ArielC','Agency','House of fun - Android','appsflyer','adcolony_int','Read\r')
  ,('Agency-Networks','Ariel Cohen','ArielC','Agency','House of fun - Android','appsflyer','vungle_int','Read\r')
  ,('Agency-Networks','Ariel Cohen','ArielC','Agency','House of fun - iOS','appsflyer','Glispa_int','Read\r')
  ,('Agency-Networks','Ariel Cohen','ArielC','Agency','House of fun - iOS','appsflyer','adcolony_int','Read\r')
  ,('Agency-Networks','Ariel Cohen','ArielC','Agency','House of fun - iOS','appsflyer','vungle_int','Read\r')
  ,('Agency-Networks','David Refael','DavidR','Agency','House of fun - Android','appsflyer',NULL,'Read\r')
  ,('Agency-Networks','David Refael','DavidR','Agency','House of fun - iOS','appsflyer',NULL,'Read\r')
  ,('Agency-Networks','Vladi Roitman','VladiR','Agency','BINGO Blitz - iOS','appsflyer',NULL,'Read\r')
  ,('Agency-Networks','Vladi Roitman','VladiR','Agency','BINGO Blitz - Android','appsflyer',NULL,'Read\r')
  ,('Agency-Networks','Vladi Roitman','VladiR','Agency','Black Diamond Casino - Android','appsflyer',NULL,'Read\r')
  ,('Agency-Networks','Vladi Roitman','VladiR','Agency','Black Diamond Casino - iOS','appsflyer',NULL,'Read\r')
  ,('Agency-Networks','Vladi Roitman','VladiR','Agency','Vegas Diamond Slots - Android','appsflyer',NULL,'Read\r')
  ,('Agency-Google','Eran Loeb','EranL','Agency','House of fun - Android','appsflyer','googleadwords_int','Read\r')
  ,('Agency-Google','Eran Loeb','EranL','Agency','House of fun - iOS','appsflyer','googleadwords_int','Read\r')
  ,('Agency-Google','Matan Ergas','MatanE','Agency','Poker Friends Texas Holdem - Android','appsflyer','googleadwords_int','Read\r')
  ,('Agency-Google','Matan Ergas','MatanE','Agency','Poker Friends Texas Holdem - iOS','appsflyer','googleadwords_int','Read\r')
  ,('Agency-Google','Matan Ergas','MatanE','Agency','Poker Friends HD - iOS','appsflyer','googleadwords_int','Read\r')
  ,('Agency-Google','Matan Ergas','MatanE','Agency','BINGO Blitz - iOS','appsflyer','googleadwords_int','Read\r')
  ,('Agency-Google','Matan Ergas','MatanE','Agency','BINGO Blitz - Android','appsflyer','googleadwords_int','Read\r')
  ,('Agency-FB','Amir K','AmirK','Agency',NULL,'appsflyer','Facebook Ads','Read\r')
  ,('Aditor-Network','Shay Y','Shay','Network',NULL,'appsflyer','aditor_int','Read\r')
  ,('Aditor-Network','Shay Y','Shay','Network',NULL,'appsflyer','trackaditor_int','Read\r');

CREATE TABLE cooladata_export_filter (offer_id VARCHAR(255), event_name VARCHAR(255), agency_pmd VARCHAR(255)
  , UNIQUE(offer_id, event_name, agency_pmd));

INSERT INTO cooladata_export_filter VALUES (NULL,'Deposit',NULL)
  ,(NULL,'FTD',NULL)
  ,(NULL,'Install',NULL);

# Indexes:
CREATE INDEX oid_ms_en_et ON appsflyer_events(offer_id, media_source, event_name, event_time) USING BTREE;
CREATE INDEX oid_ms_en_et ON appsflyer_events_full(offer_id, media_source, event_name, event_time) USING BTREE;

drop table if exists adcolony_costs_report ;

CREATE TABLE adcolony_costs_report (сampaign_id int, campaign_name VARCHAR(255), group_id int, group_name VARCHAR(255), store_id VARCHAR(255),
day_date DATETIME,	impressions	int, cvvs int, total_clicks int, ctr double, bid double, bid_type VARCHAR(50), spend double,
total_campaign_spend_limit double, total_group_spend_limit double, ecpi double, installs int,
CONSTRAINT pk_adcolony_costs_report PRIMARY KEY (сampaign_id, group_id, store_id, day_date, impressions, cvvs, total_clicks, ctr, bid ,bid_type,
spend, total_campaign_spend_limit, total_group_spend_limit, ecpi, installs));

drop table if exists heyzap_costs_report ;

CREATE TABLE heyzap_costs_report (game VARCHAR(255), day_date DATETIME, impressions	int, clicks int, installs int, cpi_price double, conversion	double, spent_usd double,
os VARCHAR(10),	сampaign_id int,  campaign_name	VARCHAR(255), campaign_status VARCHAR(10), targeted_countries VARCHAR(255), targeted_devices VARCHAR(255),
CONSTRAINT pk_heyzap_costs_report PRIMARY KEY (game, day_date, impressions, clicks, installs, cpi_price, conversion, spent_usd,
os,	сampaign_id, campaign_status, targeted_countries));

drop table if exists supersonic_costs_report ;

CREATE TABLE supersonic_costs_report (сampaign_id int, campaign_name VARCHAR(255), group_id int, group_name	VARCHAR(255), store_id VARCHAR(50), day_date DATETIME,
impressions	int, cvvs int, total_clicks int, ctr double, bid double, bid_type VARCHAR(10), 	spend_usd double, total_campaign_spend_limit double, total_group_spend_limit double,
ecpi_usd double, installs int,
CONSTRAINT pk_supersonic_costs_report PRIMARY KEY (сampaign_id, group_id, store_id, day_date, impressions, cvvs, total_clicks, ctr, bid, bid_type,
spend_usd, total_campaign_spend_limit, total_group_spend_limit, ecpi_usd, installs));

drop table if exists vungle_costs_report ;

CREATE TABLE vungle_costs_report (day_date DATETIME, campaign_id VARCHAR(16), campaign_name VARCHAR(255), impressions int, clicks int, installs int, views int,
completed_views int, daily_spend double,
CONSTRAINT pk_vungle_costs_report PRIMARY KEY (day_date, campaign_id, impressions, clicks, installs, views, completed_views, daily_spend));

drop table if exists unityads_costs_report ;

CREATE TABLE unityads_costs_report(day_date DATETIME, country_code VARCHAR(2), country_tier int, started int, views int, clicks int, installs int, spend double,
CONSTRAINT pk_vungle_costs_report PRIMARY KEY (day_date, country_code, country_tier, started, views, clicks, installs, spend));

drop table if exists applovin_costs_report2;

CREATE TABLE applovin_costs_report2 (day_date DATETIME, campaign VARCHAR(255), country VARCHAR(2), impressions int, clicks int, ctr double,
CONSTRAINT pk_costs_report PRIMARY KEY (day_date , campaign,country,impressions,clicks,ctr));

drop table if exists applovin_costs_report3;

CREATE TABLE applovin_costs_report3 (day_date DATETIME, campaign VARCHAR(255), country VARCHAR(2), impressions int, clicks int, ctr double,
CONSTRAINT pk_costs_report PRIMARY KEY (day_date , campaign,country,impressions,clicks,ctr));
