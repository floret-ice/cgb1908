function showMessage(message,type,time) {
           let str = ''
           switch (type) {
               case 'success':
                   str = '<div class="success-message" style="width: 300px;height: 40px;text-align: center;background-color:#daf5eb;;color: rgba(59,128,58,0.7);position: fixed;left: 43%;top: 10%;line-height: 40px;border-radius: 5px;z-index: 9999">\n' +
                       '    <span class="mes-text">'+message+'</span></div>'
                   break;
               case 'error':
                   str = '<div class="error-message" style="width: 300px;height: 40px;text-align: center;background-color: #f5f0e5;color: rgba(238,99,99,0.8);position: fixed;left: 43%;top: 10%;line-height: 40px;border-radius: 5px;;z-index: 9999">\n' +
                       '    <span class="mes-text">'+message+'</span></div>'
           }
           $('body').append(str)
           setTimeout(function () {
               $('.'+type+'-message').remove()
           },time)
}