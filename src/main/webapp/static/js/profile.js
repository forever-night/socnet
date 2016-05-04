/**
 * Created by anna on 02/05/16.
 */
var app = angular.module('app', ['controllers']);

app.controller('ProfileCtrl', function() {
    var self = this;

    $.isEditing = false;
    self.isEditing = false;

    self.edit = function() {
        self.isEditing = true;
    }
});