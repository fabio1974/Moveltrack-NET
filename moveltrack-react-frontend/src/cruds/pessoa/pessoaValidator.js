const formValidator = values => {
  const errors = {perfil: {}}
  if (!values.nome) {
    errors.nome = 'Campo obrigatório'
  } else if (values.nome.length > 100) {
    errors.nome = 'máx. 100 caracteres'
  } else if (values.nome.length < 5) {
    errors.nome = 'mín. 5 caracteres'
  }

  if (!values.cpf) {
    errors.cpf = 'Cpf é campo obrigatório'
  } else if (values.cpf.length != 11) {
    errors.cpf = 'Cpf tem 11 números'
  }



  if (!values.email) {
    errors.email = 'Email é obrigatório'
  } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
    errors.email = 'Email inválido'
  }

  if (!values.perfil) {
    errors.perfil.id = 'Perfil é obrigatório'
  }


  return errors
}
export default formValidator










