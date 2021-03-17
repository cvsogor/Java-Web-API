app.controller('listController', ['$scope', '$interval', '$state', '$http', function($scope, $interval, $state, $http) {
    $scope.state = 'list';

    $http({
        "method": 'GET',
        "url": server + '/api/commercials'
    }).then(function(response) {
        $scope.commercials = response.data;
        for(var i = 0; i < $scope.commercials.length; i++) {
            $scope.commercials[i].dateStart = new Date($scope.commercials[i].dateStart);
            $scope.commercials[i].dateEnd = new Date($scope.commercials[i].dateEnd);
            $scope.commercials[i].isActive = $scope.commercials[i].dateEnd ? 'Yes' : 'No';

            $scope.commercials[i].siteId = '*';
            $scope.commercials[i].campaignName = '*';

            if($scope.commercials[i]['conditions'] != null) {
                for(var j = 0; j < $scope.commercials[i]['conditions'].length; j++) {
                    if($scope.commercials[i]['conditions'][j].fieldName == 'campaign_name') {
                        $scope.commercials[i].campaignName = $scope.commercials[i]['conditions'][j].fieldValue;
                    }

                    if($scope.commercials[i]['conditions'][j].fieldName == 'site_id') {
                        $scope.commercials[i].siteId = $scope.commercials[i]['conditions'][j].fieldValue;
                    }
                }
            }
        }

        $scope.gridOptions.data = response.data;
    }, function(response) {});

    $scope.gridOptions = {
        useExternalFiltering: true,
        enableHorizontalScrollbar: 0,
        enableVerticalScrollbar: 0,
        paginationPageSize: 25,
        paginationPageSizes: [25, 50, 100],
        columnDefs: [
            {
                field: 'id', 
                displayName: 'ID', 
                type: 'number',
                cellTemplate: '<div class="ui-grid-cell-contents" tooltip = "{{row.entity.id}}" tooltip-append-to-body="true" tooltip-popup-delay="400" ><a ui-sref="details({commercialId: row.entity.id})">{{row.entity.id}}</a></div>',
                cellFilter: 'number'
            },
            {
                field: 'offerId',
                displayName: 'Offer ID',
                cellTemplate: '<div class="ui-grid-cell-contents" tooltip = "{{COL_FIELD}}" tooltip-append-to-body="true" tooltip-popup-delay="400" >{{ COL_FIELD }}</div>'
            },
            {
                field: 'mediaSource',
                displayName: 'Media Source',
                cellTemplate: '<div class="ui-grid-cell-contents" tooltip = "{{COL_FIELD}}" tooltip-append-to-body="true" tooltip-popup-delay="400" >{{ COL_FIELD }}</div>'
            },
            {
                field: 'dateStart',
                displayName: 'Start',
                cellFilter: 'date:\'yyyy-MM-dd\'',
            },
            {field: 'dateEnd', displayName: 'End', cellFilter: 'date:\'yyyy-MM-dd\''},
            {field: 'eventType', displayName: 'Event'},
            {field: 'type', displayName: 'Type'},
            {field: 'amount', displayName: 'Amount', type: 'number', cellFilter: 'currency'},
            {field: 'isActive', displayName: 'Active?'},
            {
                field: 'campaignName',
                displayName: 'Campaign Name',
                cellTemplate: '<div class="ui-grid-cell-contents" tooltip = "{{COL_FIELD}}" tooltip-append-to-body="true" tooltip-popup-delay="400" >{{ COL_FIELD }}</div>'
            },
            {field: 'siteId', displayName: 'Site ID'},
            {
                field: 'updatedOn', displayName: 'Updated On', cellFilter: 'date:\'yyyy-MM-dd hh:mm:ss\''
            }
        ]
    };

    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;

        $scope.gridApi.grid.registerRowsProcessor(function(renderableRows) {
            console.log("render");
            renderableRows.forEach(function (row) {
                var show = true;
                var entity = row.entity;
                for(var filterName in $scope.filters) {
                    if($scope.filters.hasOwnProperty(filterName) && $scope.filters[filterName].value != '') {
                        var filter = $scope.filters[filterName];
                        switch(filter.type) {
                            case 'date':
                                var timestamp_expected = filter.value.getTime();
                                var timestamp_current = entity[filter.id].getTime();
                                switch(filter.operator) {
                                    case 'equals':
                                        if(timestamp_expected != timestamp_current) {
                                            show = false;
                                        }
                                        break;
                                    case 'gte':
                                        if(timestamp_expected != timestamp_current && !(timestamp_current > timestamp_expected)) {
                                            show = false;
                                        }
                                        break;
                                    case 'lte':
                                        if(timestamp_expected != timestamp_current && !(timestamp_current < timestamp_expected)) {
                                            show = false;
                                        }
                                        break;
                                }
                                break;
                            case 'string':
                                if(String(entity[filter['id']]).toLowerCase().indexOf(filter.value.toLowerCase()) == -1) {
                                    show = false;
                                }
                                break;
                            case 'enum':
                                if(entity[filter['id']] != filter.value) {
                                    show = false;
                                }
                                break;
                        }
                    }
                }

                row.visible = show;
            });
            return renderableRows;
        }, 100);
    };

    $scope.applyFilters = function() {
        $scope.gridApi.grid.refresh();
    };

    $scope.filters = {
        'offerId': {
            id: 'offerId',
            name: 'Offer ID',
            value: '',
            operator: 'equals',
            type: 'string'
            // operators: contains equals
        },
        'mediaSource': {
            id: 'mediaSource',
            name: 'Media Source',
            value: '',
            operator: 'equals',
            type: 'string'
            // operators: contains equals
        },
        'startDate': {
            id: 'dateStart',
            name: 'Start Date',
            value: '',
            operator: 'equals',
            type: 'date'
            // operators: = >= <=
        },
        'endDate': {
            id: 'dateEnd',
            name: 'Ending Date',
            value: '',
            operator: 'equals',
            type: 'date'
            // operators: = >= <=
        },
        'eventType': {
            id: 'eventType',
            name: 'Event Type',
            value: '',
            operator: 'equals',
            type: 'enum',
            options: [
                {
                    name: 'All',
                    value: ''
                },
                {
                    name: 'First Deposit',
                    value: 'FIRST_DEPOSIT'
                },
                {
                    name: 'Install',
                    value: 'INSTALL'
                },
                {
                    name: 'Retention Deposit',
                    value: 'RETENTION_DEPOSIT'
                },
                {
                    name: 'Sign Up',
                    value: 'SIGN_UP'
                }
            ]
            // operators: select: First Deposit, Install, Retention Deposit, Sign Up
        },
        'type': {
            id: 'type',
            name: 'Commercial Type',
            value: '',
            operator: 'equals',
            type: 'enum',
            options: [
                {
                    name: 'All',
                    value: ''
                },
                {
                    name: 'Payout (we pay)',
                    value: 'PAYOUT'
                },
                {
                    name: 'Revenue (we get paid)',
                    value: 'REVENUE'
                }
            ]
            // operators: select: payout, revenue
        },
        'isActive': {
            id: 'isActive',
            name: 'Is Active',
            value: '',
            operator: 'equals',
            type: 'enum',
            options: [
                {
                    name: 'All',
                    value: ''
                },
                {
                    name: 'Yes',
                    value: 'Yes'
                },
                {
                    name: 'No',
                    value: 'No'
                }
            ]
            // operators: select: Yes, No
        },
        'campaignName': {
            id: 'campaignName',
            name: 'Campaign Name',
            value: '',
            operator: 'equals',
            type: 'string'
            // operators: contains, equals
        },
        'siteId': {
            id: 'siteId',
            name: 'Site ID',
            value: '',
            operator: 'equals',
            type: 'string'
            // operators: contains, equals
        }
    };
    $scope.clearFilters = function() {
        for(var filterName in $scope.filters) {
            if($scope.filters.hasOwnProperty(filterName)) {
                $scope.filters[filterName].value = '';
            }
        }
        $scope.applyFilters();
    };
}]);