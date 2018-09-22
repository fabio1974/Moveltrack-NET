const formValidator = values => {
  const errors = { afastamento: {} }

  if (!values.afastamentoTipo) {
    errors.afastamentoTipo = 'Campo obrigatório'
  } else if (values.afastamentoTipo.length < 4 || values.afastamentoTipo.length > 10) {
    errors.serial = 'Mínimo 4 caracteres e máximo 10'
  }
  if (!values.pessoa) {
    errors.pessoa = 'Campo obrigatório'
  }
  if (!values.inicio) {
    errors.inicio = 'Campo obrigatório'
  }
  if (!values.fim) {
    errors.fim = 'Campo obrigatório'
  }

  return errors
}
export default formValidator
