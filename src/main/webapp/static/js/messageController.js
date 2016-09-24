app.service('MessageService', function($http){
    this.get = function(senderLogin) {
        var messages = [];

        return $http.get(restUrl.publicMessage + '?sender=' + senderLogin).then(
            function (response) {
                if (response.status == 204)
                    return messages;

                var data = response.data;

                if (data != null && data.length > 0) {
                    data.forEach(function (item, i, data) {
                        var message = new Message(
                            item.id,
                            item.senderLogin,
                            item.textContent,
                            item.createdAt
                        ) ;

                        messages.push(message);
                    });
                }

                return messages;
            }
        );
    };

    this.post = function(message, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.post(restUrl.publicMessage + '/send', message, config);
    };

    this.put = function(message, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.put(restUrl.publicMessage + message.id, message, config);
    };

    this.delete = function(message, csrfToken) {
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            }
        };

        return $http.delete(restUrl.publicMessage + message.id, config);
    };
});