<script>

    var indexNavScroll = new IndexNavScroll('${excuType!}');
    indexNavScroll.start();
    jQuery(window).scroll(function(){
        indexNavScroll.start();
    }).resize(function(){
        indexNavScroll.start();
    });
    function IndexNavScroll(excuType) {
        this.excuType = excuType;
        this.$win = $(window);
        this.navbarWrapper =  $('.navbar-wrapper');
        this.navbarStaticTop =  $('.navbar-static-top',this.navbarWrapper);
        this.scrollTop = 70;
        this.mediaWidth = 768;

        this.styleDefault = function () {
            if (!this.navbarWrapper.hasClass('navbar-inverse')) {
                this.navbarWrapper.removeClass('navbar-default');
                this.navbarStaticTop.removeClass('navbar-default').addClass('navbar-inverse');
                this.navbarWrapper.removeClass('margin-top0');
            }
        };
        this.styleScrolled = function () {
            if (!this.navbarWrapper.hasClass('navbar-default')){
                this.navbarWrapper.addClass('navbar-default');
                this.navbarWrapper.addClass('margin-top0');
                this.navbarStaticTop.removeClass('navbar-inverse').addClass('navbar-default');
            }
        };

        this.start = function () {

            if(this.$win.width() >= this.mediaWidth){
                if(this.excuType == 'default'){
                    this.styleDefault();
                }else if(this.excuType == 'scrolled'){
                    this.styleScrolled();
                } else{
                    if(this.$win.scrollTop()  > this.scrollTop){
                        this.styleScrolled();
                    }else{
                        this.styleDefault();
                    }
                }
            }else{
                this.styleDefault();
            }
        };
    }
</script>