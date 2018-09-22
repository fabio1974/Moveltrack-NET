INSERT INTO public.escala_tipo(descricao, horas_de_descanso1, horas_de_descanso2, horas_de_trabalho1, horas_de_trabalho2) VALUES('24x72', 72, 0, 24, 0);
INSERT INTO public.escala_tipo(descricao, horas_de_descanso1, horas_de_descanso2, horas_de_trabalho1, horas_de_trabalho2) VALUES('12x36', 36, 0, 12, 0);

INSERT INTO public.area_organizacional(descricao, sigla, tipo, escala_tipo_id,rendicao_hora, rendicao_minuto) VALUES ('BATALHAO DE POLICIAMENTO RODOVIARIO ESTADUAL','BPRE', 'OPERACIONAL',1, 0, 0);
INSERT INTO public.area_organizacional(descricao, sigla, tipo, escala_tipo_id,rendicao_hora, rendicao_minuto) VALUES ('BATALHÃO DE POLICIAMENTO DE RONDAS DE AÇÕES INTENSIVAS E OSTENSIVAS','BPRAIO', 'OPERACIONAL',2, 7, 0);

INSERT INTO public.perfil(descricao)	VALUES ('PERFIL1'),('PERFIL2'),('PERFIL3');
INSERT INTO public.permissao(descricao)	VALUES ('ROLE1'),('ROLE2'),('ROLE3'),('ROLE4'); 

INSERT INTO public.perfil_permissaos(perfil_id, permissaos_id) VALUES (1,1),(1,2),(1,3),(2,1),(2,2),(3,1);


						

INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','tthalescesarcardoso@lognat.com.br','11111111111','1234','Curuna Koffaohi','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',1,3);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','ryanguilhermelima_@valdulion.com.br','22222222222','1235','Pyaon Pyaon','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',1,2);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','ericksergioedsonporto__ericksergioedsonporto@ipk.org.br','33333333333','1236','Ovkuon Wouadan','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',1,1);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','lleandrovitordacruz@owl-ti.com.br','62181636010','5172','Fuas Qothgai','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',1,3);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','ryanerickalexandredaconceicao-70@endoimplantes.com.br','39772350017','5741','Tuygi Miheo','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',2,2);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','ricardoantoniocalebribeiro__ricardoantoniocalebribeiro@mpeventos.com.br','08879653008','9586','Bazzuadal Ushe','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',2,1);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','muriloraimundopereira_@bravura.com.br','17454792014','7398','Edson Kaique Mendes','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',2,3);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','filipemanoelkevindaluz__filipemanoelkevindaluz@tecnew.net','16666966503','4982','Osvaldo Cláudio Mateus Figueiredo','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',2,2);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','kevinheitorantoniobarros.kevinheitorantoniobarros@silicotex.net','34046647957','7182','Daniel Anderson Juan da Rosa','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',2,1);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','otaviocalebebrenomendes_@psq.med.br','17443102038','5713','Thomas Bruno Alves','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',2,3);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','marceloigorramos_@soluxenergiasolar.com.br','96629943198','8132','Erick Elias Carvalho','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',2,2);
INSERT INTO public.pessoa(ativo, email, cpf, matricula, nome, senha, area_organizacional_id, perfil_id)	VALUES ('1','edsonkaiquemendes_@alwan.com.br','49967161426','9586','Fernando Carlos Souza','$2a$10$HVShxmGpF7NpWnCUb4MWfekCbeXR4XFZSWUIlfYW4dKCRGLkeqCHa',2,1);

INSERT INTO public.pessoa_permissaos(pessoa_id, permissaos_id) VALUES(3,4);

INSERT INTO public.arma(calibre,fabricante,modelo,registro,serial) VALUES('.40','TAUROS','PT100','A1234','112345');
INSERT INTO public.arma(calibre,fabricante,modelo,registro,serial) VALUES('.50','TAUROS','PT200','B1234','212345');
INSERT INTO public.arma(calibre,fabricante,modelo,registro,serial) VALUES('.60','TAUROS','PT300','C1234','312345');
INSERT INTO public.arma(calibre,fabricante,modelo,registro,serial) VALUES('.70','TAUROS','PT400','D1234','412345');

INSERT INTO public.chip(iccid,numero,operadora) VALUES('89551090418228584687','(85)98208-6808','VIVO');
INSERT INTO public.rastreador(imei,ativo_bloqueio,ativo_botao_panico,ativo_cerca_virtual,ativo_escuta,rastreador_tipo,chip_id) VALUES('000000205778292',false,false,false,false,'ST500',1);

INSERT INTO public.viatura(chassi,cor,marca_modelo,placa,rastreador_id) VALUES('BW510904182285846','VERMELHO','LOGAN','ORX4379',1);
INSERT INTO public.viatura(chassi,cor,marca_modelo,placa) VALUES('CW510904182285846','VERMELHO','CELTA','ABC1234');
INSERT INTO public.viatura(chassi,cor,marca_modelo,placa) VALUES('DW510904182285846','ROXO','KOMBI','BBB4379');
INSERT INTO public.viatura(chassi,cor,marca_modelo,placa) VALUES('FW510904182285846','AZUL','HILUX','CCC4379');
INSERT INTO public.viatura(chassi,cor,marca_modelo,placa) VALUES('GW510904182285846','AMARELO','LIFAN','DDD4379');
INSERT INTO public.viatura(chassi,cor,marca_modelo,placa) VALUES('HW510904182285846','CINZA','FUSCA','EEE4379');






