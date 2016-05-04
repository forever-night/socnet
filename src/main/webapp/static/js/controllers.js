/**
 * Created by anna on 23/04/16.
 */
var app = angular.module('app', []);

app.controller('MainCtrl', function () {
    var self = this;

    self.auth = false;

    this.setAuth = function(value) {
        self.auth = value;
    }
});