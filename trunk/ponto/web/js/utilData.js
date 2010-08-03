function verificaMes(){
    // Declara variáveis
    var data,ano,mes,bissexto,meses,qnt;
    // Obtém a data atual
    data=new Date();
    // Obtém o ano atual
    ano=data.getFullYear();
    // Obtém o mês atual
    mes=data.getMonth();
    // Verifica se o ano é bissexto, para definir a quantidade de dias do mês Fevereiro
    bissexto=(((ano%4)==0&&(ano%100)!=0)||(ano%400)==0)?29:28;
    // Array das quantidades de dias de cada mês
    meses=new Array(31,bissexto,31,30,31,30,31,31,30,31,30,31);
    // Quantidade de dias do mês atual
    qnt=meses[mes];
    // Imprime
    return qnt;
    //document.write("O mês atual contém "+qnt+" dias");
}


