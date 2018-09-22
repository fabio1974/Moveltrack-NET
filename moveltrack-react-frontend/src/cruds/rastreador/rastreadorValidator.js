const formValidator = values => {
  const errors = { rastreador: {} }
  if (!values.serial) {
    errors.serial = 'Campo obrigatório'
  } else if (values.serial.length < 7 || values.serial.length > 8) {
    errors.serial = 'Mínimo 7 caracteres e máximo 8'
  }
  if (!values.registro) {
    errors.registro = 'Campo obrigatório'
  } else if (values.registro.length != 9) {
    errors.registro = 'O registro deve possuir 9 caracteres'
  }
  if (!values.fabricante) {
    errors.fabricante = 'Campo obrigatório'
  } else if (values.fabricante.length < 5 || values.fabricante.length > 20) {
    errors.fabricante = 'Mínimo de caracteres e máximo 20'
  }
  if (!values.modelo) {
    errors.modelo = 'Campo obrigatório'
  } else if (values.modelo.length < 3 || values.modelo.length > 10) {
    errors.modelo = 'Mínimo de caracteres 3 e máximo 10'
  }
  if (!values.calibre) {
    errors.calibre = 'Campo obrigatório'
  } else if (values.calibre.length < 2 || values.calibre.length > 4) {
    errors.calibre = 'Mínimo de caracteres 2 e máximo 4'
  }
  return errors
}
export default formValidator
