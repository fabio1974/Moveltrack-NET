const formValidator = values => {
  const errors = { escalaTipo: {} }
  if (!values.horasDeTrabalho1) {
    errors.horasDeTrabalho1 = 'Campo obrigatório'
  } else if (values.horasDeTrabalho1.length > 2) {
    errors.horasDeTrabalho1 = 'Máximo de caracteres 2'
  }
  if (!values.horasDeDescanso1) {
    errors.horasDeDescanso1 = 'Campo obrigatório'
  } else if (values.horasDeDescanso1.length > 2) {
    errors.horasDeDescanso1 = 'Máximo de caracteres 2'
  }
  return errors
}
export default formValidator
