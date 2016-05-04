/**
 * Created by anna on 23/04/16.
 */
var app = angular.module('app', ['controllers']);

app.controller('SignInCtrl', function() {
    var self = this;

    main.setAuth(false);
});