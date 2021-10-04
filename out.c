#include<stdio.h>
#include<string.h>
#include<stdlib.h>

int output(int flag,char* buffer){
    if(flag == 0){
        if(strcmp(buffer,"=")==0)
            printf("Assign");
        else if(strcmp(buffer,"==")==0)
            printf("Eq");
        else if(strcmp(buffer,";")==0)
            printf("Semicolon");
        else if(strcmp(buffer,"(")==0)
            printf("LPar");
        else if(strcmp(buffer,")")==0)
            printf("RPar");
        else if(strcmp(buffer,"{")==0)
            printf("LBrace");
        else if(strcmp(buffer,"}")==0)
            printf("RBrace");
        else if(strcmp(buffer,"+")==0)
            printf("Plus");
        else if(strcmp(buffer,"*")==0)
            printf("Mult");
        else if(strcmp(buffer,"/")==0)
            printf("Div");
        else if(strcmp(buffer,"<")==0)
            printf("Lt");
        else if(strcmp(buffer,">")==0)
            printf("Gt");
        else{
            printf("Err\n");
            return -1;
        }
        printf("\n");

    }
    else if(flag == 1){
        if(strcmp(buffer,"if")==0)
            printf("If");
        else if(strcmp(buffer,"else")==0)
            printf("Else");
        else if(strcmp(buffer,"while")==0)
            printf("While");
        else if(strcmp(buffer,"break")==0)
            printf("Break");
        else if(strcmp(buffer,"continue")==0)
            printf("Continue");
        else if(strcmp(buffer,"return")==0)
            printf("Return");
        else{
            printf("Ident(%s)\n",buffer);
            return 0;
        }
        printf("\n");
        
        
    }
    else if(flag == 2){
         printf("Number(%s)\n",buffer);

    }
    return 0;
}


int isLetter(char c) { 
    if((c >='a'&& c <= 'z') ||(c>='A'&& c <= 'Z'))
        return 1;
    return 0;
}
int isDigit(char c){
    
    if(c >= '0' && c <= '9')
        return 1;
    return 0;
}

int isUnderline(char c){
    return c == '_';
}

int isNodigit(char c){
    return isLetter(c) || isUnderline(c);
}

int getIdentifier(FILE* f, char c){
   int i = 1;
    char *p = (char*)malloc(sizeof(char)*100);
    p[0] = c;
    while((c = fgetc(f)) != EOF) {
        
        if(isDigit(c) || isNodigit(c)){
            p[i++] = c;
        }
        else{
            fseek(f,-1,SEEK_CUR);
            p[i] = '\0';
            output(1,p);
            return 0;
        }
    }
    p[i] = '\0';
    output(1,p);
    return 0;
}

int getUnsignInt(FILE* f, char c){
    int i = 1;
    char *p = (char*)malloc(sizeof(char)*100);
    p[0] = c;
    while((c = fgetc(f)) != EOF){
        if(isDigit(c)){
            p[i++] = c;
        }
        else{
            fseek(f,-1,SEEK_CUR);
            p[i] = '\0';
            output(2,p);
            return 0;
        }
    }
    p[i] = '\0';
    output(2,p);
    return 0;
}



char* getWord(FILE* fp){
    
    char* p;
    int i = 1;
    char c;
    while(( c = fgetc(fp)) != EOF ){
       
        p = (char*)malloc(sizeof(char)*100);
        if(c == ' ' || c == '\n')
            continue;
        else{
            if(isDigit(c)){
                getUnsignInt(fp,c);
            }
            else if(isNodigit(c)){
                getIdentifier(fp,c);
            }
            else if(c != '='){
                p[0] = c;
                p[1] = '\0';
                if(output(0,p) == -1)
                    return NULL;
            }
            else{
                c = fgetc(fp);
                if(c == '=')
                    output(0,"==");
                else{
                    output(0,"=");
                    if(c != EOF)
                        fseek(fp,-1,SEEK_CUR);
                }
            }

            
        }
    }
}


int main(int argc, char** argv){
    if(argc != 2){
        printf("parameter err!--------------------------------------------------------");
        return 1;
    }
    char* file = argv[1];
    
    FILE * fp1 = fopen(file, "r");
    if(fp1 == NULL){
        printf("open file err!--------------------------------------------------------");
        return 1;
    }
    getWord(fp1);
   
    return 0;
    

}