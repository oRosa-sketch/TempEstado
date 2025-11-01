Projeto: TempEstado App
TempEstado é um aplicativo Android (nativo Java) de previsão do tempo, desenvolvido como projeto de faculdade. O aplicativo exibe a previsão do tempo para os próximos dias, consome dados de uma API (HG Brasil), exibe um mapa da cidade consultada e permite ao usuário trocar de cidade escaneando um QR Code.

Funcionalidades Principais
Splash Screen: Tela de abertura de 3 segundos.

Navegação por Abas: Tela principal com duas abas (Previsão e Mapa) usando ViewPager2 e TabLayout.

Menu Superior: Menu na AppBar para acessar a tela "Sobre".

Consumo de API Rest: Busca dados de previsão do tempo da API HG Brasil usando Retrofit e Gson.

Lista customizada: Exibe a previsão em um RecyclerView com itens em CardView.

Mapa: Exibe um mapa da cidade consultada usando OpenStreetMap (osmdroid).

Scanner de QR Code: Usa um FloatingActionButton e a biblioteca Zxing para ler um QR Code e atualizar a cidade consultada.

Design: Utiliza componentes do Material Design.

Bibliotecas Utilizadas
com.google.android.material:material (Componentes de UI, Abas, Cards, FAB)

androidx.appcompat:appcompat (Suporte base do Android)

androidx.viewpager2:viewpager2 (Gerenciador de Abas deslizáveis)

androidx.recyclerview:recyclerview (Lista de previsão)

com.squareup.retrofit2:retrofit (Cliente HTTP para API)

com.squareup.retrofit2:converter-gson (Conversor JSON para objetos Java)

org.osmdroid:osmdroid-android (Biblioteca do Mapa - OpenStreetMap)

androidx.preference:preference (Usado pelo OSM para gerenciamento de cache)

com.journeyapps:zxing-android-embedded (Biblioteca do Scanner de QR Code)

Estrutura do Projeto
O projeto está organizado na seguinte estrutura de pacotes (pastas) para facilitar a manutenção:

com.faculdade.tempestado
├── activity/      (Controladores de Telas - Activities)
├── adapter/       (Adaptadores para listas e abas)
├── fragment/      (Controladores das "sub-telas" das abas)
├── model/         (Classes POJO que representam os dados da API)
└── network/       (Classes responsáveis pela comunicação com a API)
Explicação do Código
Abaixo está o detalhamento de cada arquivo e suas funções principais.

Pacote activity
Classes que controlam as "telas" principais do aplicativo.

AberturaActivity.java
Controla a tela de Splash Screen.

onCreate(Bundle): Define o layout da tela (R.layout.activity_abertura) e chama iniciarTimerSplash().

iniciarTimerSplash(): Usa um Handler para criar um atraso de 3 segundos (TEMPO_SPLASH). Após o tempo, ele cria uma Intent para abrir a MainActivity e chama finish() para fechar a tela de abertura, impedindo que o usuário volte para ela.

SobreActivity.java
Controla a tela "Sobre o Desenvolvedor".

onCreate(Bundle): Define o layout (R.layout.activity_sobre) e chama configurarToolbar().

configurarToolbar(): Localiza a Toolbar no layout e a define como a ActionBar da tela, ativando o botão "Voltar" (setDisplayHomeAsUpEnabled).

onOptionsItemSelected(MenuItem): Detecta cliques nos itens da Toolbar. Se o item clicado for o botão "Voltar" (android.R.id.home), ele chama finish() para fechar a tela e retornar à MainActivity.

MainActivity.java
A tela principal e o cérebro do app. Gerencia as abas, o menu e o scanner de QR Code.

Variáveis Principais:

qrCodeLauncher: O novo método do Android para registrar e receber o resultado do scanner de QR Code (registerForActivityResult).

Funções Principais:

onCreate(Bundle): Carrega o layout (R.layout.activity_main), inicializa a configuração do OpenStreetMap (Configuration.getInstance().load), encontra todos os componentes de UI (findViewById), define a Toolbar e chama configurarAbas() e solicitarPermissoes(). Também define o setOnClickListener do fabQrCode para chamar iniciarScanner().

iniciarScanner(): Configura o ScanOptions da biblioteca ZXing. Define que queremos apenas QR Codes (setDesiredBarcodeFormats(ScanOptions.QR_CODE)) e o texto do prompt. Em seguida, inicia o qrCodeLauncher.

processarResultadoQrCode(String): Chamado quando o qrCodeLauncher retorna um sucesso. Ele recebe o texto lido, quebra o texto pela vírgula (,) e valida se o formato está correto (ex: Campinas,SP,-22.90,-47.06). Se estiver, ele chama notificarFragments().

notificarFragments(...): O "comunicador" do app. Ele percorre todos os fragments anexados à MainActivity e verifica quais deles implementam a interface AtualizavelFragment. Para os que implementam, ele chama o método atualizarCidade(), passando os novos dados.

configurarAbas(): Cria uma instância do ViewPagerAdapter, define-o no ViewPager2 e usa o TabLayoutMediator para conectar as abas (TabLayout) ao ViewPager2, definindo os títulos ("Previsão" e "Mapa").

onCreateOptionsMenu(Menu): Infla (cria) o menu de 3 pontinhos a partir do arquivo R.menu.main_menu.

onOptionsItemSelected(MenuItem): Detecta o clique no item de menu "Sobre" (R.id.action_sobre) e inicia a SobreActivity.

solicitarPermissoes(): Verifica se o app já tem as permissões de CAMERA, WRITE_EXTERNAL_STORAGE (para o mapa) e ACCESS_FINE_LOCATION. Se não tiver, ele exibe a caixa de diálogo do Android pedindo-as ao usuário.

Pacote fragment
Classes que controlam as "sub-telas" (conteúdo) dentro das abas da MainActivity.

AtualizavelFragment.java (Interface)
É um "contrato". Ele força qualquer Fragment que o implemente a ter um método chamado atualizarCidade(...). Isso permite que a MainActivity se comunique com os fragments sem saber exatamente o que eles fazem.

PrevisaoFragment.java
Controla a aba "Previsão".

onCreateView(...): Carrega (infla) o layout R.layout.fragment_previsao.

onViewCreated(...): Chamado após o layout ser criado. Ele encontra o RecyclerView e o ProgressBar, configura o LinearLayoutManager e o PrevisaoAdapter no RecyclerView, e chama buscarPrevisao() pela primeira vez.

buscarPrevisao(String cidade): Método principal. Torna o ProgressBar visível. Usa o RetrofitClient para criar uma chamada de API (call.enqueue()).

onResponse(): Se a API responder com sucesso, ele esconde o ProgressBar, limpa a lista antiga, adiciona os novos dados da previsão (response.body()) e notifica o PrevisaoAdapter (adapter.notifyDataSetChanged()) para redesenhar a lista.

onFailure(): Se a rede falhar, esconde o ProgressBar e exibe um Toast de erro.

atualizarCidade(...): (Método obrigatório da interface). É chamado pela MainActivity após o scan do QR Code. Ele simplesmente atualiza a cidadeAtual e chama buscarPrevisao() novamente com o novo nome da cidade.

MapaFragment.java
Controla a aba "Mapa".

onCreateView(...): Carrega (infla) o layout R.layout.fragment_mapa.

onViewCreated(...): Encontra o MapView (mapa OSM), define a fonte dos "azulejos" do mapa (TileSourceFactory.MAPNIK), ativa os controles de zoom e chama atualizarMapa().

atualizarMapa(): Define o ponto central (setCenter) e o nível de zoom do mapa. Limpa marcadores antigos (mapView.getOverlays().clear()) e cria um novo Marker na posição (cidadeCoordenadas) e título (cidadeNome).

atualizarCidade(...): (Método obrigatório da interface). É chamado pela MainActivity. Ele atualiza as variáveis cidadeNome e cidadeCoordenadas e chama atualizarMapa() para centralizar o mapa no novo local.

onResume() / onPause(): Métodos essenciais do ciclo de vida do osmdroid para garantir que o mapa pause e libere recursos corretamente quando o usuário troca de aba ou sai do app.

Pacote adapter
Classes que gerenciam a exibição de dados em componentes complexos (listas e abas).

ViewPagerAdapter.java
Gerencia os fragments dentro do ViewPager2.

createFragment(int position): Retorna a instância do Fragment correto para cada aba. Posição 0 retorna new PrevisaoFragment(), Posição 1 retorna new MapaFragment().

getItemCount(): Retorna o número total de abas (2).

PrevisaoAdapter.java
Gerencia a exibição dos dados da API dentro do RecyclerView.

onCreateViewHolder(...): Cria uma nova "caixa" (View) para um item da lista, inflando o layout R.layout.item_previsao.

onBindViewHolder(...): Pega os dados de PrevisaoDiaria na position específica e preenche os TextViews (Data, Dia, Descrição, Max, Min) do ViewHolder correspondente.

getItemCount(): Retorna quantos itens existem na previsaoList.

PrevisaoViewHolder (Classe interna): "Segura" (holds) as referências dos TextViews do layout item_previsao.xml para acesso rápido, evitando findViewById repetitivos.

Pacote model (POJOs)
Classes Java simples (Plain Old Java Objects) que servem como "molde" para o Gson (Conversor JSON). Os nomes das variáveis e as anotações @SerializedName devem bater com as chaves do JSON da API.

PrevisaoDiaria.java: Armazena os dados de um dia (date, weekday, max, min, description).

ResultsData.java: Armazena a lista de previsão (List<PrevisaoDiaria> forecast).

PrevisaoResponse.java: Armazena o objeto raiz da resposta da API (ResultsData results).

Pacote network
Classes que gerenciam a conexão com a API HG Brasil.

HgBrasilApiService.java (Interface)
Define quais chamadas podemos fazer na API.

getPrevisao(...): Define um método que fará uma requisição GET para o endpoint "weather" e que envia dois parâmetros de query: "key" e "city_name". O Retrofit espera que a resposta seja um objeto PrevisaoResponse.

RetrofitClient.java
Define como vamos nos conectar à API (padrão Singleton).

getApiService(): Um método estático que verifica se a instância do Retrofit já existe. Se não, ele a cria, define a URL base (BASE_URL) e o conversor (GsonConverterFactory). Ele então retorna o serviço (HgBrasilApiService) pronto para ser usado.

Arquivos de Recurso (Principais)
res/layout/
activity_main.xml: Define a estrutura visual da tela principal, contendo a Toolbar, o TabLayout (para as abas), o ViewPager2 (para o conteúdo das abas) e o FloatingActionButton.

fragment_previsao.xml: Contém o RecyclerView para a lista e um ProgressBar para o feedback de carregamento.

fragment_mapa.xml: Contém o componente org.osmdroid.views.MapView que desenha o mapa.

item_previsao.xml: Define o layout de um item da lista. É um MaterialCardView contendo os TextViews para os dados da previsão.

res/menu/main_menu.xml
Define o item "Sobre" (action_sobre) e instrui o Android a nunca mostrá-lo como um ícone, mas sim dentro do menu de 3 pontinhos (app:showAsAction="never").

AndroidManifest.xml
O "RG" do aplicativo.

Permissões: Define tudo o que o app precisa acessar (Internet, Câmera, Localização, Armazenamento).

Declaração de Activities: Registra as 3 activities (AberturaActivity, MainActivity, SobreActivity) para que o Android saiba que elas existem.

Intent Filter (Launcher): Define a AberturaActivity como a primeira tela a ser aberta.

Correção do Bug ZXing: A parte mais importante. Ele identifica a CaptureActivity (da biblioteca ZXing) e usa tools:replace="screenOrientation" para forçar que ela obedeça à nossa regra (android:screenOrientation="portrait"), corrigindo o bug da câmera de lado.# TempEstado
