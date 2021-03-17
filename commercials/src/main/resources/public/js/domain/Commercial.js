function Commercial() {
    this.id = null;
    this.offerId = "";
    this.mediaSource = "";
    this.dateStart = new Date();
    this.dateEnd = new Date();
    this.eventType = EventType.INSTALL;
    this.type = CommercialType.PAYOUT;
    this.isActive = true;
    this.conditions = [];
}

function CommercialBuilder() {
    this.commercial = new Commercial();

    this.withOfferId = function(offerId) {
        this.commercial.offerId = offerId;
        return this;
    };

    this.withMediaSource = function(mediaSource) {
        this.commercial.mediaSource = mediaSource;
        return this;
    };

    this.withDateStart = function(dateStart) {
        this.commercial.dateStart = dateStart;
        return this;
    };

    this.withDateEnd = function(dateEnd) {
        this.commercial.dateEnd = dateEnd;
        return this;
    };

    this.withEventType = function(eventType) {
        this.commercial.eventType = eventType;
        return this;
    };

    this.withCommercialType = function(commercialType) {
        this.commercial.type = commercialType;
        return this;
    };

    this.withActive = function(isActive) {
        this.commercial.isActive = isActive;
        return this;
    };

    this.withCondition = function(targetCondition) {
        this.conditions.push(targetCondition);
        return this;
    };

    this.build = function () {
        return this.commercial;
    }
}