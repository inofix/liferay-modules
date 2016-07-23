module.exports = function(grunt) {
    
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    
    grunt.initConfig({
        
        copy : {
            jquery : {
                files: [{cwd: 'bower_components/jquery/dist',  src: ['**/*.min.js'], dest: 'src/main/webapp/js', expand: true }]           
            },
            leaflet : {
                files: [
                    {cwd: 'bower_components/leaflet/dist',  src: ['images/*', 'leaflet.js'], dest: 'src/main/webapp/js', expand: true },
                    {cwd: 'bower_components/leaflet/dist',  src: ['leaflet.css'], dest: 'src/main/webapp/css', expand: true }
                ]                           
            }
        },
        
        jshint : {
            files : [ 'Gruntfile.js', 'src/main/js/*.js',
                    'src/test/javascript/*.js' ]
        }
    });
    
    grunt.registerTask('default', [ 'jshint', 'copy']);
};