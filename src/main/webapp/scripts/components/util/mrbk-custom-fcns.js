function GOC() {

}

GOC.prototype = {
    browserBasedHtmlDate: function (prop) {
        
        var datePicker = null;
        if (window.chrome) {
            datePicker = '<input type="date" class="form-control" name="'+prop.name+'" id="'+prop.id+'" ng-model="'+prop.ngModel+'"  placeholder="Yıl-ay-gün">';
        } else {
            datePicker = '<input type="date" class="form-control" name="'+prop.name+'" id="'+prop.id+'" ng-model="'+prop.ngModel+'" data-provide="datepicker" placeholder="Yıl-ay-gün">';
        }
        $('#'+prop.formGroupId).append(datePicker);
    }
}

window.goc = new GOC();