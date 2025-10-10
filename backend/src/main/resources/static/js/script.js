(function(){
    var key='theme';
    var current=localStorage.getItem(key)||'dark';
    function apply(){document.documentElement.dataset.theme=current;}
    document.addEventListener('click',function(e){
        if(e.target && e.target.id==='toggle-theme'){
            current=current==='dark'?'light':'dark';
            localStorage.setItem(key,current);
            apply();
        }
    });
    apply();
})();


