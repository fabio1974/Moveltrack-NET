const formValidator = values => {
  const errors = { }
  if (!values.descricao) {
    errors.nome = 'Campo obrigatório'
  } else if (values.descricao.length > 200) {
    errors.nome = 'máx. 200 caracteres'
  } else if (values.descricao.length < 5) {
    errors.nome = 'mín. 5 caracteres'
  }

  if (!values.sigla) {
    errors.sigla = 'A Sigla é obrigatória'
  } else if (values.sigla.length > 10) {
    errors.sigla = 'Máx  10 caracteres'
  }

  if (!values.tipo) {
    errors.tipo = 'Campo obrigatório'
  }

  if (!values.escalaTipo) {
    errors.escalaTipo = 'Campo obrigatório'
  }

    return errors
}
export default formValidator










