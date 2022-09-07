# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and this project adheres to the following versioning pattern:

Given a version number MAJOR.MINOR.PATCH, increment:

- MAJOR version when the **API** version is incremented. This may include backwards incompatible changes;
- MINOR version when **breaking changes** are introduced OR **new functionalities** are added in a backwards compatible manner;
- PATCH version when backwards compatible bug **fixes** are implemented.


## [Unreleased]
### Added
- BrcodePreview resource
- StaticBrcode resource
- DynamicBrcode resource
- CreditPreview sub-resource
- CreditNotePreview sub-resource
- CardMethod sub-resource
- MerchantCountry sub-resource
- MerchantCategory sub-resource
- flow parameter to PixClaim resource
- flow parameter to query and page methods in PixClaim resource
- tags parameter to PixChargeback, PixClaim and PixInfraction resources
- tags parameter to query and page methods in PixChargeback, PixClaim and PixInfraction resources
- tags parameter to PixClaim, PixInfraction, Pix Chargeback, DynamicBrcode and StaticBrcode resources
- code attribute to IssuingProduct resource
- nominalInterest attribute to CreditNote resource
- zipCode, purpose, isPartialAllowed, cardTags and holderTags attributes to IssuingPurchase resource
- brcode, link and due attributes to IssuingInvoice resource
### Changed
- IssuingBin resource to IssuingProduct
- fine and interest attributes to return only in CreditNote.Invoice sub-resource
- expiration from returned-only attribute to optional parameter in the CreditNote resource
- settlement parameter to fundingType and client parameter to holderType in Issuing Product resource
- bankCode parameter to claimerBankCode in PixClaim resource
- agent parameter to flow in PixClaim and PixInfraction resources
- agent parameter to flow on query and page methods in PixClaim resource
- Creditnote.Signer sub-resource to CreditSigner resource
### Removed
- IssuingAuthorization resource
- category parameter from IssuingProduct resource
- agent parameter from PixClaim.Log resource
- bacenId parameter from PixChargeback and PixInfraction resources

## [0.1.0] - 2022-06-03
### Added
- credit receiver's billing address on CreditNote

## [0.0.3] - 2022-05-23
### Added
- PixDomain resource for Indirect and Direct Participants
- code attribute to IssuingProduct resource
- PixDirector resource for Direct Participants
- CreditNote.Signer sub-resource
- CreditNote.Invoice sub-resource
- CreditNote.Transfer sub-resource
- issuinginvoice.Log.get() function to IssuingInvoice Log resource
- Webhook resource to receive Events
- merchantFee atribute to IssuingPurchase
### Changed
- CreditNote.transfer parameter to payment and paymentType
- InfractionReport resource name to PixInfraction
- ReversalRequest resource name to PixChargeback
- PixInfraction and PixChargeback to post in batches
- delete methods name to cancel

## [0.0.2] - 2022-05-02
### Added
- PixKey resource for Indirect and Direct Participants
- PixClaim resource for Indirect and Direct Participants
- InfractionReport resource for Indirect and Direct Participants
- ReversalRequest resource for Indirect and Direct Participants
- get(), query(), page(), delete() and update() functions to Event resource.
- Event.Attempt sub-resource to allow retrieval of information on failed webhook event delivery attempts
- CreditNote resource for money lending with Stark's Infra endorsement.
- IssuingAuthorization resource for Sub Issuers
- IssuingBalance resource for Sub Issuers
- IssuingBin resource for Sub Issuers
- IssuingCard resource for Sub Issuers
- IssuingHolder resource for Sub Issuers
- IssuingInvoice resource for Sub Issuers
- IssuingPurchase resource for Sub Issuers
- IssuingTransaction resource for Sub Issuers

## [0.0.1] - 2022-03-16
### Added
- PixRequest resource for Indirect and Direct Participants
- PixReversal resource for Indirect and Direct Participants
- PixBalance resource for Indirect and Direct Participants
- PixStatement resource for Direct Participants
- Event resource for webhook receptions