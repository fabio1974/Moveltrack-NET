const formValidator = values => {
  const errors = {viatura: {}}
  if (!values.placa) {
    errors.placa = 'Campo obrigatório'
  } else if (values.placa.length != 7) {
    errors.placa = 'Campo deve conter 7 caracteres'
  }

  if (!values.marcaModelo) {
    errors.marcaModelo = 'Marca/Modelo é campo obrigatório'
  } else if (values.marcaModelo.length < 3 || values.marcaModelo.length > 10) {
    errors.marcaModelo = 'Mínimo de caracteres 3 e máximo 10'
  }

  if (values.chassi  && values.chassi.length != 17) {
    errors.chassi = 'Campo deve contar 17 caracteres'
  }

  if (!values.cor) {
    errors.cor = 'Cor é obrigatório'
  } else if (values.cor.length < 4 || values.cor.length > 8) {
    errors.cor = 'Mínimo de caracteres 4 e máximo 8'
  }

  return errors
}
export default formValidator
