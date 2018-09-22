export default {
  items: [



    {
      name: 'Cadastros',
      url: '/base',
      icon: 'gi gi-file-contract-o',
      children: [
        {
          name: 'Área Organizacional',
          url: '/areaOrganizacionals',
          icon: 'material-icons',
          iconValue: 'insert_chart'
        },
        {
          name: 'Pessoas',
          url: '/pessoas',
          icon: 'gi gi-users',
        },
        // {
        //   name: 'Curriculos',
        //   url: '/curriculos',
        //   icon: 'gi gi-cac-o',
        // },
        {
          name: 'Armas (Uso Restrito)',
          url: '/armas',
          icon: 'gi gi-gun',
        },
        {
          name: 'Afastamentos',
          url: '/afastamentos',
          icon: 'fa fa-calendar',
        }
      ],
    },
    {
      name: 'Escalas',
      //url: '/escalaConfig',
      icon: 'gi gi-clock-o',

      children: [
        {
          name: 'Criar Modelo',
          url: '/escalaTipos',
          icon: 'gi gi-clock-o',
        },
        {
          name: 'Criar Grupos',
          url: '/escalaConfig',
          icon: 'fa fa-users',
        },
        {
          name: 'Gerenciar Escalas',
          url: '/escalas',
          icon: 'gi gi-table',
        },

      ]


    },



    {
      name: 'Operações',
      url: '/operacaos',
      icon: 'gi gi-gears',
    },
    {
      name: 'Monitoramento',
      url: '/base',
      icon: 'gi gi-satellite',
      children: [
        {
          name: 'Viaturas',
          url: '/viaturas',
          icon: 'fa fa-cab',
        },

        {
          name: 'Rastreador',
          url: '/rastreadors',
          icon: 'material-icons',
          iconValue: 'speaker_phone'
        },


        {
          name: 'Chip de Rastreador',
          url: '/chips',
          icon: 'material-icons',
          iconValue: 'sd_storage '
        },


        {
          name: 'Mapa',
          url: '/mapa',
          icon: 'gi gi-world',
        },

      ]
      }    

  ],
};
