var gulp = require('gulp');
var gulpConcat = require('gulp-concat');

gulp.task('default', function () {
    gulp.src('js/**/*.js')
        .pipe(gulpConcat('app.min.jsx'))
        .pipe(gulp.dest('.'));
});