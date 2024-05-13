import { IUserMySuffix, NewUserMySuffix } from './user-my-suffix.model';

export const sampleWithRequiredData: IUserMySuffix = {
  id: 16964,
};

export const sampleWithPartialData: IUserMySuffix = {
  id: 23025,
  email: 'Patience_Casper-Hammes47@yahoo.com',
  address: 'gee swiftly ew',
  profilePhoto: '../fake-data/blob/hipster.png',
  profilePhotoContentType: 'unknown',
};

export const sampleWithFullData: IUserMySuffix = {
  id: 4138,
  email: 'Enoch.Rohan-Kohler99@yahoo.com',
  password: 'righteously lobotomize knowledgeably',
  address: 'never boo below',
  contact: 'transcribe sometimes pfft',
  profilePhoto: '../fake-data/blob/hipster.png',
  profilePhotoContentType: 'unknown',
};

export const sampleWithNewData: NewUserMySuffix = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
