if(!window.myPlugin){
    window.myPlugin = {};
}

/**
 * 表单验证的构造函数
 * 通过该构造函数，创建一个表单验证对象
 */
window.myPlugin.FormValidator = function(option){
    //默认配置
    var defaultOption = {
        formDom: document.forms[0],//表示获取当前页面的第一个表单
        formRule:{},//默认表单规则空对象
        errorClass: "field-error"//错误类名
    };
    this.option = Object.assign({},defaultOption,option);//混合 形成最终的配置

    //注册各种事件
    var elems = this.getAllElements();
    var that = this;
    for(var i = 0; i < elems.length; i ++){
        var elem = elems[i];
        var field = elem.field;
        (function(field){//监听事件是异步的 等待事件触发才会执行 先要形成闭包将每一次的field保存起来
            elem.doms.forEach(function(el){
                //循环每一个dom元素注册事件
                var name = that.getEventName(el);
                var fields = [field];//触发事件时要验证的字段
                var configName = myPlugin.FormValidator.dataConfig.dataFieldTrigger;
                var triggers = el.getAttribute(configName);
                if(triggers){
                    triggers = triggers.split(",");
                    fields = fields.concat(triggers);//将数组拼接
                }
                el.addEventListener(name,function(){
                    that.setStatus.apply(that,fields);
                });
            });
        }(field));
    }
}

/**
 * 获取事件名
 */
myPlugin.FormValidator.prototype.getEventName = function(el){
    var name = myPlugin.FormValidator.dataConfig.dataFieldListener;
    var eventName = el.getAttribute(name);
    if(!eventName){
        eventName = myPlugin.FormValidator.dataConfig.dataFieldDefaultListener;
    }
    return eventName;
}


/**
 * 得到所有需要验证的表单元素
 */
myPlugin.FormValidator.prototype.getAllElements = function(){
    var containers = this.getAllContainers();//得到所有的表单容器
    var result = [];//最终结果
    for(var i = 0; i < containers.length;i++){
        var con = containers[i];
        var obj = {field: con.getAttribute(myPlugin.FormValidator.dataConfig.fieldContainer)};
        obj.doms = this.getFieldElement(con);
        result.push(obj);
    }
    return result;
}


/**
 * 得到一个表单字段的数据 如果没有拿到任何表单数据 则返回null
 * @param {string} field 表单容器中的表单字段名
 */
myPlugin.FormValidator.prototype.getFieldData = function(field){
    //找寻表单字段的容器
    var fieldContainer = this.getFieldContainer(field);
    if(!fieldContainer){
        return;
    }
    var eles = this.getFieldElement(fieldContainer);
    var datas = [];//数据数组
    eles.forEach(function(element){
        var propName = myPlugin.FormValidator.dataConfig.dataFieldProp;//得到自定义属性名
        //使用dataset获取其元素上的自定义属性 比如data-field-prop 得到dom对象 使用dom.dataset获取到fieldProp
        //然后判断这里的属性名是否在着自定义属性中有(用户是否按规则填写) 若有则将其值取出 赋值给propName 若没有则使用默认value
        //这里的propName是指元素中的属性名 比如img元素 img下有src属性 要获取其属性值先在其标签下写 data-field-prop="src" src="..." 这样就可以获取到其属性值
        // if(propName in element.dataset){
        //     propName = element.dataset[propName];//从dataset的对象中将其值取出
        // }else{
        //     propName = myPlugin.FormValidator.dataConfig.dataFieldDefaultProp;//使用默认
        // }
        //由于使用dataset获取到的是小驼峰命名法 所以这里改用 getAttribute
        propName = element.getAttribute(propName);//获取到属性值
        if(!propName){
            propName = myPlugin.FormValidator.dataConfig.dataFieldDefaultProp;//使用默认
        }
        var val = element[propName];//通过属性名获取到其属性值
        //需要判断单选框和多选框
        if(element.type == "checkbox" || element.type == "radio"){
            //因为单选或多选都值需要其选中的内容 不选的不要
            if(element.checked){
                datas.push(val);
            }
        }else{
            datas.push(val);//将每个元素的要得到的属性值放入数组
        }
    });
    if(datas.length === 0){//如果没有拿到任何表单数据 则返回null
        return null;
    }
    if(eles.length === 1){
        return datas[0];//只有一个表单元素 返回数组第一项
    }
    return datas;
}

/**
 * 得到整个表单数据
 */
myPlugin.FormValidator.prototype.getFormData = function(){
    var dataName = myPlugin.FormValidator.dataConfig.fieldContainer;
    var containers = this.getAllContainers();
    var that = this;
    var formData = {};
    containers.forEach(function(con){//循环得到的容器
        var fieldName = con.getAttribute(dataName);//获取字段名
        var data = that.getFieldData(fieldName);//字段值
        formData[fieldName] = data;
    });
    return formData;
}

/**
 * 得到所有的表单容器
 */
myPlugin.FormValidator.prototype.getAllContainers = function(){
    //拿到所有的表单域容器
    var containers = this.option.formDom.querySelectorAll("["+myPlugin.FormValidator.dataConfig.fieldContainer+"]");
    return Array.from(containers);
}


/**
 * 得到一个表单字段容器
 * @param {string} field 表单字段名
 */
myPlugin.FormValidator.prototype.getFieldContainer = function(field){
    return this.option.formDom.querySelector("["+myPlugin.FormValidator.dataConfig.fieldContainer+"="+field+"]");
}

/**
 * 得到表单字段元素
 * @param {object} fieldContainer 表单域容器
 */
myPlugin.FormValidator.prototype.getFieldElement = function(fieldContainer){
    //得到给定的表单域容器中的表单字段元素 像是checkbox有可能有多个
    var eles = fieldContainer.querySelectorAll("["+myPlugin.FormValidator.dataConfig.dataField+"]");//通过属性选择器 找寻所有属性中由此自定义属性的字段
    return Array.from(eles);
}


//验证=========================

/**
 * 验证一个数据
 * @param {*} data 要验证的数据
 * @param {object} ruleObj 验证规则的对象
 * @param {object} formData 整个表单数据
 * @returns 返回验证结果，如果验证通过，返回true，如果没有通过，返回一个错误信息
 */
myPlugin.FormValidator.prototype.validateData = function(data,ruleObj,formData){
    //data: null,普通数据，数组
    //判断ruleObj的rule属性值的类型
    if(typeof ruleObj.rule === "string"){
        //规则为预设值
        // var names = Object.getOwnPropertyNames(myPlugin.FormValidator.validators);//获取到对象的所有属性名
        var func = myPlugin.FormValidator.validators[ruleObj.rule];//通过预设值去取对应的方法
        if(!func){//预设值无效
            throw new TypeError("验证规则不正确");
        }
        if(func(data,formData)){
            return true;
        }
        return ruleObj.message;
    }else if(ruleObj.rule instanceof RegExp){
        //规则为正则
        //正则会将null，数组转化为字符串进行验证 数组可以不用管一般只会对一个单值进行验证 需要判断一下null
        if(data === null){
            return ruleObj.message;//返回错误消息
        }
        if(ruleObj.rule.test(data)){
            return true;
        }
        return ruleObj.message;
    }else if(typeof ruleObj.rule === "function"){
        //自定义函数
        return ruleObj.rule(data,formData);//直接调用将参数传入 让用户自己进行判断
    }
    throw new TypeError("验证规则不正确");
}

/**
 * 验证某个字段，返回一个验证结果，返回true，如果验证没有通过，返回验证信息
 * 验证信息: 字段名，数据，规则对象，错误信息
 */
myPlugin.FormValidator.prototype.validateField = function(field,formData){
    var data = formData[field];//要验证的数据
    var ruleObjs = this.option.formRule[field];//验证规则数组
    if(!ruleObjs){//没有验证规则证明不需要验证 直接返回true
        return true;
    }
    for(var i = 0; i < ruleObjs.length;i++){
        var ruleObj = ruleObjs[i];
        var result = this.validateData(data,ruleObj,formData);
        if(result !== true){//有错误，result是错误信息
            return{//验证错误的各种信息
                field: field,
                data: data,
                ruleObj: ruleObj,
                message: result
            }
        }
    }
    return true;//验证通过
}

/**
 * 验证表单
 */
myPlugin.FormValidator.prototype.validate = function(){
    var formData = this.getFormData();//得到所有的表单数据
    if(arguments.length === 0){//没有参数
        var fields = Object.getOwnPropertyNames(formData);//获得表单中所有的属性名
    }else{
        var fields = Array.from(arguments);
    }
    var that = this;
    var results = fields.map(function(field){//map映射循环数组元素 将其根据函数内容映射到数组中
        return that.validateField(field,formData);
    }).filter(function(item){//[true,{},true,{}] 最后只需要错误信息把true去掉
        return item !== true;
    });
    return results;
}

/**
 * 设置某个表单项的状态
 * @param {*} validateResult 该表单项的错误信息，如果是undefined,表示没有错误
 * @param {*} field 验证的表单项名称
 */
myPlugin.FormValidator.prototype.setFieldStatus = function(validateResult,field){
    var fieldContainer = this.getFieldContainer(field);//表单字段容器
    var errorEle = fieldContainer.querySelector("["+myPlugin.FormValidator.dataConfig.dataFieldError+"]");
    if(!errorEle){
        errorEle = fieldContainer.querySelector("."+myPlugin.FormValidator.dataConfig.dataFieldDefaultError);
    }
    if(validateResult){//有错误
        if(errorEle){
            errorEle.innerHTML = validateResult.message;
        }
        fieldContainer.classList.add(this.option.errorClass);
    }else{//无错误
        fieldContainer.classList.remove(this.option.errorClass);
        if(errorEle){
            errorEle.innerHTML = "";
        }
    }
}

/**
 * 设置整个表单的状态
 * 无参: 整个表单
 * 有参: 根据参数设置具体的表单项
 */
myPlugin.FormValidator.prototype.setStatus = function(){
    if(arguments.length === 0){
        var formData = this.getFormData();
        var fields = Object.getOwnPropertyNames(formData);//拿到表单中的所有字段
    }else{
        var fields = Array.from(arguments); //字段来自于参数传递
    }
    var results = this.validate.apply(this,fields);
    var that = this;
    fields.forEach(function(field){
        var res = results.find(function(item){//在检测出问题的数组中差找
            return item.field === field;//找到用户检测的信息 若不传入参数fields多于item.field 找到其中的相同的参数
        });//从验证结果中，找寻某个字段的验证结果，没有找到，自然为undefined
        that.setFieldStatus(res,field);
    })
    return results;
}


/**
 * 自定义属性的名字
 */
myPlugin.FormValidator.dataConfig = {
    fieldContainer: "data-field-container", //表单字段容器的自定义属性名
    dataField: "data-field", //表单字段的自定义属性名
    dataFieldProp: "data-field-prop", //要验证的表单字段的属性名 要从此获取自定义属性名的值
    dataFieldDefaultProp: "value", //要验证的表单字段默认的属性名
    dataFieldListener: "data-field-listener", //要监听的事件的自定义属性名
    dataFieldDefaultListener: "change", //要监听的默认事件
    dataFieldTrigger: "data-field-trigger", //要额外触发的验证字段
    dataFieldDefaultTrigger: "", //要额外触发的验证字段的默认值(不触发)
    dataFieldError: "data-field-error", //错误信息的元素
    dataFieldDefaultError: "error" //错误信息的默认元素的类名
}

/**
 * 预设的验证规则，通过返回ture，没有通过返回false 需要填充可以随时增加
 */
myPlugin.FormValidator.validators = {
    required: function(data){//非空验证
        if(!data){
            return false;
        }
        if(Array.isArray(data) && data.length === 0){
            return false;
        }
        return true;
    },
    mail: function(data){//邮箱
        if(data === null){
            return false;
        }
        //123asd@qq.com.sda
        var reg = /^\w+@\w+(\.\w+){1,2}$/;
        return reg.test(data)
    },
    number: function(data){//数字
        var reg = /^\d+(\.\d+)?$/;
        return reg.test(data);
    }
}