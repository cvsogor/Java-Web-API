app.directive('autoComplete', function($timeout){
    return function (scope, element, attributes) {
        $(element).autocomplete({
            source: function(request, response) {
                console.log(attributes.items);

                var array = scope[attributes.items];

                try {
                    var result = JSON.parse(attributes.items);
                    array = result;
                } catch (error) {
                    array = scope[attributes.items];
                }

                response(array.filter(function(arrayElement, index, array) {
                    return String(arrayElement).toLowerCase().indexOf(request.term.toLowerCase()) !== -1;
                }));
            },
            select: function(event, ui) {
                $timeout(function() {
                    var hierarchy = attributes.ngModel.split('.');
                    var object = scope[hierarchy[0]];
                    for(var i = 1; i < hierarchy.length - 1; i++) {
                        object = object[hierarchy[i]];
                    }

                    object[hierarchy[hierarchy.length - 1]] = ui.item.value;
                }, 0)
            }
        });
    }
});